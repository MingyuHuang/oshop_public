package com.melon.configuration;

public enum PaymentMethod {

    ALIPAY("Alipay", TradeType.WEBSITE),
    WECHAT("Wechat pay", TradeType.WEBSITE),
    PAYPAL("PayPal", TradeType.WEBSITE),
    IDEAL("Ideal", TradeType.WEBSITE),
    CREDIT_CARD("Credit Card", TradeType.POS),
    ;

    private String method;
    private TradeType tradeType;

    PaymentMethod(String method, TradeType tradeType) {
        this.method = method;
        this.tradeType = tradeType;
    }

    public String getMethod() {
        return method;
    }

    public TradeType getTradeType() {
        return tradeType;
    }
}
