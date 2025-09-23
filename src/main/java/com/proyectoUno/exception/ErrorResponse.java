package com.proyectoUno.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Representa una respuesta de error estandarizada para enviar a los clientes.
 * Proporciona un formato JSON consistente para las excepciones de la API.
 */

public class ErrorResponse {

    /**campos de la respuesta */

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp; //Fecha y hora en que ocurrio la excepcion
    private  int status; //Codigo de estado Http
    private String error; //Error Http asociado
    private String message; // Mnesaje personalizado descriptivo de la excepcion;
    private String path; //URL de la solicitud que origino la excepcion;
    private String internalCode; //Identificador de la ecepcion lanzada

    /** Detalles adicionales opcionales sobre la excepción */
    private Map<String, String> details ; //Detalles adicionales sobre la excepcion


    /**
     * Constructor base sin detalles adicionales.
     *
     * @param status Código HTTP
     * @param message Mensaje descriptivo
     * @param path URL de la solicitud
     * @param internalCode Código interno de la excepción
     */
    public ErrorResponse(HttpStatus status, String message, String path, String internalCode){

        this.timestamp=LocalDateTime.now();
        this.status=status.value();
        this.error=status.getReasonPhrase();
        this.message=message;
        this.path=path;
        this.internalCode=internalCode;
    }

    /**
     * Constructor con detalles adicionales.
     * @param status Código HTTP
     * @param message Mensaje descriptivo
     * @param path URL de la solicitud
     * @param internalCode Código interno de la excepción
     * @param details Detalles adicionales sobre la excepción
     */
    public ErrorResponse(HttpStatus status, String message, String path, String internalCode,Map<String,String> details){

        this(status,message,path,internalCode);
        this.details =details;

    }

    //metodo set para details

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    //getters

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public Map<String, String> getDetails() {
        return details;
    }
}
