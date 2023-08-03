package com.melon.configuration;

public enum TradeType {

    MOBILE("Mobile"),
    WEBSITE("Website"),
    POS("Pos"),
    APP("App"),
    ;

    private String type;
    TradeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
