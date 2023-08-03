package com.melon.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melon.configuration.PaymentMethod;
import com.melon.configuration.TradeType;
import com.melon.entity.PaymentInfo;
import com.melon.repository.PaymentInfoRepository;
import com.melon.service.PaymentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class PaymentInfoServiceImpl implements PaymentInfoService {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    @Override
    public void createPaymentInfoAlipay(final Map<String, String> params) throws JsonProcessingException {

        PaymentInfo paymentInfo = PaymentInfo.builder()
                .orderId(params.get("out_trade_no"))
                .transactionId(params.get("trade_no"))
                .paymentMethod(PaymentMethod.ALIPAY.getMethod())
                .tradeType(TradeType.WEBSITE.getType())
                .tradeStatus(params.get("trade_status"))
                .payerTotal(Double.parseDouble(params.get("total_amount")))
                .content(mapper.writeValueAsString(params))
                .build();
        paymentInfoRepository.saveAndFlush(paymentInfo);
    }
}
