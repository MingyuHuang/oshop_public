package com.melon.exception;

public enum OrderExceptionMessage {

    NO_ORDER("No order by this id"),
    PAYMENT_PAGE_CREATION_FAILED("Failed to create Alipay payment page"),
    PAYMENT_CANCELLATION_FAILED("Failed to cancel this payment"),
    SIGNATURE_VERIFICARION_FAILED("Failed to verify the signature of this transaction"),
    PRICE_VERIFICATION_FAILED("Failed to verify the price"),
    SELLERID_VERIFICATION_FAILED("Failed to verify the seller-id"),
    APPID_VERIFICATION_FALIED("Falied to verify the app-id"),
    TRANSACTION_FAILED("Failed to process this transaction"),
    ;

    private String message;
    OrderExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
