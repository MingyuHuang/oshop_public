package com.melon.configuration;

public enum OrderStatus {

    PAID("Paid"),
    UNPAID("Unpaid"),
    CANCEL("Cancel"),
    CLOSE("Close"),
    ;

    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
