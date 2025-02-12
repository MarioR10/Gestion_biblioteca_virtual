package com.proyectoUno.exception;

public class ListaDeEntidadesVaciaException extends  RuntimeException {

    public ListaDeEntidadesVaciaException (String message){

        super(message);
    }

    public ListaDeEntidadesVaciaException (String message,Throwable cause){

        super(message, cause);
    }

    public ListaDeEntidadesVaciaException (Throwable cause){

        super(cause);
    }

}
