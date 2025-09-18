package com.proyectoUno.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import org.springframework.security.core.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> manejadorAutenticacionFallida( AuthenticationException ex, WebRequest request){

        //Extraemos la URL de donde proviene la excepcion
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorResponse errorResponse= new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                path,
                "AUTH_ERROR"
        );
        return  new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejadorExcepcionesGenericas( Exception ex, WebRequest request){

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrio un error inesperado: " + ex.getMessage(),
                path,
                "INTERNAL_ERROR"
        );
        return  new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

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

    @ExceptionHandler(EntidadDuplicadaException.class)
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

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> manejadorConflict(ConflictException ex, WebRequest request){

        //Obtenemos la URL donde se origino la excepcion
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        //Detalles adicionales de la excepcion
        Map<String, String> details = new HashMap<>();
        if( ex.getCampoBusqueda() != null) details.put("ENTIDAD", ex.getEntidadNombre());
        if( ex.getCampoBusqueda() != null) details.put("CAMPO_BUSQUEDA", ex.getCampoBusqueda());
        if( ex.getValorCampoBusqueda() != null) details.put("VALOR_CAMPO", ex.getValorCampoBusqueda().toString());

        //Creamos un error estandar
        ErrorResponse errorResponse= new ErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                path,
                ex.getInternalCode(),
                details
        );

        return  new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}

