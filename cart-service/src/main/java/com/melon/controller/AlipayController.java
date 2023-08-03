package com.melon.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.melon.configuration.PaymentResult;
import com.melon.dto.R;
import com.melon.entity.Order;
import com.melon.exception.OrderExceptionMessage;
import com.melon.service.AlipayService;
import com.melon.service.OrderService;
import com.melon.service.PaymentInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/ali-pay")
@Api(tags = "Web Alipay API")
@Slf4j
public class AlipayController {

    @Autowired
    private AlipayService alipayService;
    @Autowired
    private Environment environment;
    @Autowired
    private OrderService orderService;

    @PostMapping("/trade/page/pay")
    @ApiOperation("Generate payment page")
    public R tradePagePay(@RequestBody String orderId) {

        String form = alipayService.createTrade(orderId);
        return R.ok().data("form", form);
    }

    @ApiOperation("Cancel Order")
    @PostMapping("/trade/close/{orderId}")
    public R cancel (@PathVariable String orderId){

        alipayService.cancelOrder(Long.parseLong(orderId));
        return R.ok().message("Cancel Successfully");
    }

    @PostMapping("/trade/notify")
    @ApiOperation("Payment notification")
    public String tradeNotify(@RequestParam Map<String, String> params) throws AlipayApiException, JsonProcessingException {

        String result = PaymentResult.FAILURE.getMessage();
        boolean isVerified = AlipaySignature.rsaCheckV1(params, environment.getProperty("alipay.alipay-public-key"), AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);
        if (isVerified){
            log.info(PaymentResult.SIGNATURE_VERIFICATION_SUCCESS.getMessage());

            String outTradeNo = params.get("out_trade_no");
            Order order = orderService.getOrderById(Long.parseLong(outTradeNo));
            if (order == null){
                log.error(OrderExceptionMessage.NO_ORDER.getMessage());
                return result;
            }
            Double totalAmountAlipay = Double.parseDouble(params.get("total_amount"));
            Double totalAmount = order.getItems().stream()
                    .map(item -> item.getTotalPrice())
                    .collect(Collectors.summingDouble(price -> price));
            if (!totalAmountAlipay.equals(totalAmount)){
                log.error(OrderExceptionMessage.PRICE_VERIFICATION_FAILED.getMessage());
                return result;
            }

            String sellerId = params.get("seller_id");
            String localSellerId = environment.getProperty("alipay.seller-id");
            if (!sellerId.equals(localSellerId)){
                log.error(OrderExceptionMessage.SELLERID_VERIFICATION_FAILED.getMessage());
                return result;
            }

            String appId = params.get("app_id");
            String localAppId = environment.getProperty("alipay.app-id");
            if (!appId.equals(localAppId)){
                log.error(OrderExceptionMessage.APPID_VERIFICATION_FALIED.getMessage());
                return result;
            }

            String tradeStatus = params.get("trade_status");
            if (!"TRADE_SUCCESS".equals(tradeStatus)){
                log.error(OrderExceptionMessage.TRANSACTION_FAILED.getMessage());
                return result;
            }

            alipayService.processOrder(params);

            result = PaymentResult.SUCCESS.getMessage();
        }else{
            log.error(OrderExceptionMessage.SIGNATURE_VERIFICARION_FAILED.getMessage());
        }
        return result;
    }

//    iqecnu6892@sandbox.com
//    gusode7253@sandbox.com
}
