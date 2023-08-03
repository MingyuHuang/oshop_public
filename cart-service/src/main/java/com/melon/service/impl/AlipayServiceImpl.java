package com.melon.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.melon.configuration.OrderStatus;
import com.melon.configuration.PaymentMethod;
import com.melon.entity.OrderItem;
import com.melon.entity.Order;
import com.melon.exception.OrderExceptionMessage;
import com.melon.service.AlipayService;
import com.melon.service.OrderService;
import com.melon.service.PaymentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentInfoService paymentInfoService;
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private Environment environment;
    private final ReentrantLock lock = new ReentrantLock();

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createTrade(String orderId) throws RuntimeException{

        try{

            Order order = orderService.getOrderById(Long.parseLong(orderId));
            orderService.updateOrderPaymentMethod(Long.parseLong(orderId), PaymentMethod.ALIPAY.getMethod());
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setReturnUrl(environment.getProperty("alipay.return-url"));
            request.setNotifyUrl(environment.getProperty("alipay.notify-url"));

            request.setBizContent(populateBizContent(order).toString());
            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            if (response.isSuccess()){

                return response.getBody();
            }else{
                throw new RuntimeException(OrderExceptionMessage.PAYMENT_PAGE_CREATION_FAILED.getMessage());
            }
        } catch (NullPointerException exception){
            throw new RuntimeException(OrderExceptionMessage.NO_ORDER.getMessage());
        } catch (AlipayApiException exception){
            throw new RuntimeException(OrderExceptionMessage.PAYMENT_PAGE_CREATION_FAILED.getMessage());
        }
    }

    private JSONObject populateBizContent(Order order){

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", order.getId());
        Double totalAmount  = order.getItems().stream()
                .map(item -> item.getTotalPrice())
                .collect(Collectors.summingDouble(price -> price));
        bizContent.put("total_amount", totalAmount);
        bizContent.put("subject", "Payment Test");
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        JSONArray ordersDetail = populateOrdersDetail(order);
        bizContent.put("goods_detail", ordersDetail);
        return bizContent;
    }

    private JSONArray populateOrdersDetail(Order order){
        JSONArray itemsDetail = new JSONArray();
        for (OrderItem item: order.getItems()){

            JSONObject product = new JSONObject();
            product.put("goods_id", item.getTitle());
            product.put("goods_name", item.getTitle());
            product.put("quantity", item.getQuantity());
            product.put("price", item.getTotalPrice());
            itemsDetail.add(product);
        }
        return itemsDetail;
    }

    @Override
    public void cancelOrder(Long orderId) {

        this.closeOrder(orderId);
        orderService.updateOrderStatus(orderId, OrderStatus.CANCEL.getStatus());
    }

    private void closeOrder(Long orderId){

        try {
            AlipayTradeCloseRequest closeRequest = new AlipayTradeCloseRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", orderId);
            closeRequest.setBizContent(bizContent.toString());
            AlipayTradeCloseResponse response = alipayClient.execute(closeRequest);
            if (response.isSuccess()){
                log.info("Cancel order successfully");
            }else{
                log.error("Failed to cancel order");
                throw new RuntimeException(OrderExceptionMessage.PAYMENT_CANCELLATION_FAILED.getMessage());
            }
        }catch (AlipayApiException exception){
            throw new RuntimeException(OrderExceptionMessage.PAYMENT_CANCELLATION_FAILED.getMessage());
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void processOrder(Map<String, String> params) {

        log.info("processing order..");
        if (lock.tryLock()){

            try{
                String outTradeNo = params.get("out_trade_no");
                Order order = orderService.getOrderById(Long.parseLong(outTradeNo));
                String orderStatus = order.getOrderStatus();
                if (!OrderStatus.UNPAID.getStatus().equals(orderStatus)){
                    return;
                }
                orderService.updateOrderStatus(Long.parseLong(outTradeNo),OrderStatus.PAID.getStatus());
                paymentInfoService.createPaymentInfoAlipay(params);
            } catch (JsonProcessingException exception){

            } catch (Exception exception){

            }finally {
                lock.unlock();
            }
        }
    }
}
