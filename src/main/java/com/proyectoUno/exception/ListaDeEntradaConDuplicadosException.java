package com.proyectoUno.exception;

public class ListaDeEntradaConDuplicadosException extends RuntimeException {
    public ListaDeEntradaConDuplicadosException(String message) {

        super(message);
    }

    public ListaDeEntradaConDuplicadosException (String message,Throwable cause){

        super(message, cause);
    }

    public ListaDeEntradaConDuplicadosException (Throwable cause){

        super(cause);
    }

}
