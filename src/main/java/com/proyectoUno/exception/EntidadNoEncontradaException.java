package com.proyectoUno.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;


public class EntidadNoEncontradaException extends  RuntimeException{

    public EntidadNoEncontradaException(String message){

        super(message);
    }

    public EntidadNoEncontradaException(String message,Throwable cause){

        super(message, cause);
    }

    public EntidadNoEncontradaException(Throwable cause){

        super(cause);
    }

}
