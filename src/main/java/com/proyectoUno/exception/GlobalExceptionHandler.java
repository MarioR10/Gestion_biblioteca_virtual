package com.proyectoUno.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.core.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que maneja globalmente todas las excepciones de la aplicación.
 * Genera respuestas estandarizadas con formato JSON usando ErrorResponse.
 * Cada método maneja un tipo específico de excepción y define el código HTTP correspondiente.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja errores de autenticación.
     * Devuelve 401 Unauthorized con un mensaje detallado.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> manejadorAutenticacionFallida( AuthenticationException ex, WebRequest request){

        //Extraemos la URL de donde proviene la excepcion
        String path = obtenerURL(request);
        ErrorResponse errorResponse= new ErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                path,
                "AUTH_ERROR"
        );
        return  new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);

    }

    /**
     * Maneja cualquier excepción genérica no controlada.
     * Devuelve 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejadorExcepcionesGenericas( Exception ex, WebRequest request){

        String path = obtenerURL(request);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrio un error inesperado: " + ex.getMessage(),
                path,
                "INTERNAL_ERROR"
        );
        return  new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja excepciones cuando una entidad no es encontrada.
     * Devuelve 404 Not Found con detalles de la entidad buscada.
     */
    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> manejadorEntidadNoEncontrada(EntidadNoEncontradaException ex, WebRequest request){

        //Extraemos la URl de donde proviene la excepcion
        String path = obtenerURL(request);

        //Detalles especificos de la excepcion
        Map<String, String> details=crearDetails(ex.getEntidadNombre(),ex.getCampoBusqueda(),ex.getValorCampoBusqueda());

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

    /**
     * Maneja excepciones cuando una entidad duplicada es detectada.
     * Devuelve 409 Conflict con detalles de la entidad duplicada.
     */
    @ExceptionHandler(EntidadDuplicadaException.class)
    public ResponseEntity<ErrorResponse> manejadorEntidadDuplicada ( EntidadDuplicadaException ex, WebRequest request){

        //Obtenemos la URL donde ocurrio la excepcion
        String path = obtenerURL(request);

        //Detalles especificos de la excepcion
        Map<String, String> details=crearDetails(ex.getEntidadNombre(),ex.getCampoBusqueda(),ex.getValorCampoBusqueda());

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

    /**
     * Maneja solicitudes de actualización inválidas.
     * Devuelve 400 Bad Request con mensaje explicativo.
     */
    @ExceptionHandler(SolicitudActualizacionInvalidaException.class)
    public ResponseEntity<ErrorResponse> manejadorSolicitudActualizacionInvalida(SolicitudActualizacionInvalidaException ex, WebRequest request){

        //Obtenemos la URL donde ocurrio la excepcion
        String path = obtenerURL(request);

        //Creamos el error estandar
        ErrorResponse errorResponse= new ErrorResponse(

                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                path,
                ex.getInternalCode()
        );

        return  new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja solicitudes que contienen duplicados.
     * Devuelve 409 Conflict.
     */
    @ExceptionHandler(SolicitudConDuplicadosException.class)
    public ResponseEntity<ErrorResponse> manejadorSolicitudConDuplicados(SolicitudConDuplicadosException ex, WebRequest request){

        //Obtenemos la URL donde ocurrio la excepcion
        String path = obtenerURL(request);

        //Creamos el error estandar
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                path,
                ex.getInternalCode()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Maneja conflictos específicos detectados durante operaciones.
     * Devuelve 409 Conflict con detalles adicionales de la entidad y campo.
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> manejadorConflict(ConflictException ex, WebRequest request){

        //Obtenemos la URL donde se origino la excepcion
        String path = obtenerURL(request);

        //Detalles adicionales de la excepcion
        Map<String, String> details=crearDetails(ex.getEntidadNombre(),ex.getCampoBusqueda(),ex.getValorCampoBusqueda());

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

    /* ==========================
       MÉTODOS PRIVADOS AUXILIARES
       ========================== */

    /**
     * Extrae la URL de la solicitud desde el objeto WebRequest.
     */
    private String obtenerURL(WebRequest request){
        return  ((ServletWebRequest) request).getRequest().getRequestURI();

    }

    /**
     * Construye un mapa con los detalles adicionales de la excepción.
     * Solo incluye valores no nulos.
     */
    private Map<String,String> crearDetails(String entidad, String campoBusqueda, Object valorCampo){

        Map<String, String> details = new HashMap<>();

        if( entidad!= null) details.put("ENTIDAD" , entidad);
        if( campoBusqueda != null) details.put("CAMPO_BUSQUEDA",campoBusqueda);
        if( valorCampo != null) details.put("VALOR_CAMPO",valorCampo.toString());

        return details;
    }
}

