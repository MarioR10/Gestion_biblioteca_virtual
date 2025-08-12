package com.proyectoUno.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtRevokedException extends AuthenticationException {
    public JwtRevokedException(String message) {
        super(message);
    }
}
