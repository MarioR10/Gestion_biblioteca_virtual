package com.proyectoUno.security.exception;

import org.springframework.security.core.AuthenticationException;

public class CustomAuthException extends AuthenticationException {
    public CustomAuthException(String message, Throwable cause) {
        super(message);
    }
}
