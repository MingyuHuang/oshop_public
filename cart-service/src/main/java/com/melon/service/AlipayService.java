package com.melon.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface AlipayService {

    String createTrade(String orderId);

    void processOrder(Map<String, String> params);

    void cancelOrder(Long parseLong);
}
