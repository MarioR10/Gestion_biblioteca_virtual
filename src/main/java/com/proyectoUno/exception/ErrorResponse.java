package com.proyectoUno.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.jsonwebtoken.security.Message;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Representa una respuesta de error estandarizada para enviar a los clientes.
 * Proporciona un formato JSON consistente para las excepciones de la API
 *
 *
 */

public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp; //Fecha y hora en que ocurrio la excepcion
    private  int status; //Codigo de estado Http
    private String error; //Error Http asociado
    private String message; // Mnesaje personalizado descriptivo de la excepcion;
    private String path; //URL de la solicitud que origino la excepcion;
    private String internalCode; //Identificador de la ecepcion lanzada

    //Mas detalles sobre la excepcion (opcional)
    private Map<String, String> details ; //Detalles adicionales sobre la excepcion


    /**
     * Constructor  base para las excepciones
     * @param status
     * @param message
     * @param path
     * @param internalCode
     */
    ErrorResponse(HttpStatus status, String message, String path, String internalCode){

        this.timestamp=LocalDateTime.now();
        this.status=status.value();
        this.error=status.getReasonPhrase();
        this.message=message;
        this.path=path;
        this.internalCode=internalCode;
    }

    /**
     * Constructor con detalles adicionales
     * @param status
     * @param message
     * @param path
     * @param internalCode
     * @param details
     */

    ErrorResponse(HttpStatus status, String message, String path, String internalCode,Map<String,String> details){

        this(status,message,path,internalCode);
        this.details =details;

    }

    //metodo set para details

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}
