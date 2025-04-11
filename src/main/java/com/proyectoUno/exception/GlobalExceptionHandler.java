package com.proyectoUno.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<String> manejarEntidadNoEncontrada(EntidadNoEncontradaException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);  // Devuelve un mensaje de error y un código 404
    }

    @ExceptionHandler(ListaDeEntidadesVaciaException.class)
    public ResponseEntity<String> manejarListaEntidadesVacia( ListaDeEntidadesVaciaException ex){

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT); //Devuelve un mensaje de error y un codigo 204


    }

    @ExceptionHandler(EntidadDuplicadaException.class)
    public ResponseEntity<String> manejarEntidadDuplicada( EntidadDuplicadaException ex){


        return  new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(PaginaDeEntidadesVaciaException.class)
    public ResponseEntity<String> manejarPaginaVacia( PaginaDeEntidadesVaciaException ex){

        return  new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);


    }
}
