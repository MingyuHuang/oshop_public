package com.melon.exception;

import org.springframework.security.core.AuthenticationException;

public class UserAlreadyExistAuthenticationException extends AuthenticationException {


    public UserAlreadyExistAuthenticationException(String msg) {
        super(msg);
    }
}
