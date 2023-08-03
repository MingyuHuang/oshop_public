package com.melon.exception;

public enum CartServiceExceptionMessage {

    NO_CART("Shopping Cart with this id is not found in DB"),
    ;

    private String message;

    CartServiceExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
