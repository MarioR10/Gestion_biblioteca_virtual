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
}
