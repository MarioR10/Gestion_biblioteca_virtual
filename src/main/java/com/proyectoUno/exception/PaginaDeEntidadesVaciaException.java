package com.proyectoUno.exception;

public class PaginaDeEntidadesVaciaException extends  RuntimeException{


    public PaginaDeEntidadesVaciaException(String message){

        super(message);
    }

    public PaginaDeEntidadesVaciaException(String message,Throwable cause){

        super(message, cause);
    }

    public PaginaDeEntidadesVaciaException(Throwable cause){

        super(cause);
    }



}
