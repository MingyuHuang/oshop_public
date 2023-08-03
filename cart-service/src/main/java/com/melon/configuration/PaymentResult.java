package com.melon.configuration;

public enum PaymentResult {

    SUCCESS("success"),
    FAILURE("failure"),
    SIGNATURE_VERIFICATION_SUCCESS("Succeed in verifying signature"),
    ;

    private String message;
    PaymentResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
