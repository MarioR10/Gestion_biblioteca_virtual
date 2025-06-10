package com.proyectoUno.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> manejadorEntidadNoEncontrada(EntidadNoEncontradaException ex, WebRequest request){

        //Extraemos la URl de donde proviene la excepcion
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        //Detalles especificos de la excepcion
        Map<String, String> details = new HashMap<>();
        if (ex.getEntidadNombre() != null) details.put("ENTIDAD: ", ex.getEntidadNombre());
        if (ex.getCampoBusqueda() !=null ) details.put("CAMPO_BUSQUEDA: ", ex.getCampoBusqueda());
        if (ex.getValorCampoBusqueda() !=null ) details.put("VALOR_CAMPO: ", ex.getValorCampoBusqueda().toString());

        //Crea una respuesta de error estandar
        ErrorResponse errorResponse= new ErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                path,
                ex.getInternalCode(),
                details
        );
        return  new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ErrorResponse> manejadorEntidadDuplicada ( EntidadDuplicadaException ex, WebRequest request){

        //Obtenemos la URL donde ocurrio la excepcion
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        //Detalles especificos de la excepcion
        Map<String, String> details = new HashMap<>();

        if( ex.getEntidadNombre() != null) details.put("ENTIDAD" , ex.getEntidadNombre());
        if( ex.getCampoBusqueda() != null) details.put("CAMPO_BUSQUEDA",ex.getCampoBusqueda());
        if( ex.getCampoBusqueda() != null) details.put("VALOR_CAMPO",ex.getValorCampoBusqueda().toString());

        //Creamos una respuesta de Error estandar
        ErrorResponse errorResponse = new ErrorResponse(

                HttpStatus.CONFLICT,
                ex.getMessage(),
                path,
                ex.getInternalCode(),
                details
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(SolicitudActualizacionInvalidaException.class)
    public ResponseEntity<ErrorResponse> manejadorSolicitudActualizacionInvalida(SolicitudActualizacionInvalidaException ex, WebRequest request){

        //Obtenemos la URL donde ocurrio la excepcion
        String path = ((ServletWebRequest) request) .getRequest().getRequestURI();

        //Creamos el error estandar
        ErrorResponse errorResponse= new ErrorResponse(

                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                path,
                ex.getInternalCode()
        );

        return  new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SolicitudConDuplicadosException.class)
    public ResponseEntity<ErrorResponse> manejadorSolicitudConDuplicados(SolicitudConDuplicadosException ex, WebRequest request){

        //Obtenemos la URL donde ocurrio la excepcion
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        //Creamos el error estandar
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                path,
                ex.getInternalCode()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);

    }
}

