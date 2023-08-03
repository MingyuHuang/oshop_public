package com.melon.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface PaymentInfoService {

    void createPaymentInfoAlipay(Map<String, String> params) throws JsonProcessingException;
}
