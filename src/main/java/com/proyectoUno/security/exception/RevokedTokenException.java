package com.proyectoUno.security.exception;

import org.springframework.security.core.AuthenticationException;

public class RevokedTokenException  extends AuthenticationException {

    public RevokedTokenException(String message){
        super(message);
    }
}
