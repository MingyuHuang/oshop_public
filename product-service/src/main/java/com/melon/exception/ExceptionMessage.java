package com.melon.exception;

public enum ExceptionMessage {

    EMPTY_JWT("JWT is empty"),
    NO_AUTHORIZATION_HEADER("No Authorization header provided"),
    EXPIRED_JWT("JWT is expired, please log in again"),
    NO_USER("User does not exist"),
    ;

    private String message;
    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
