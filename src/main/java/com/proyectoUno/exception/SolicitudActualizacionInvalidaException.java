package com.proyectoUno.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolicitudActualizacionInvalidaException extends  RuntimeException {

    //logger que permite capturar las excepciones en consola
    private final Logger logger = LoggerFactory.getLogger(SolicitudActualizacionInvalidaException.class);

    //ID de la excepcion
    private final String internalCode = "SOLICITUD_DE_ACTUALIZACION_INVALIDA";

    //Nombre de la entidad a Actualizar
    private final String entidadNombre;


    public SolicitudActualizacionInvalidaException( String entidadNombre){
        super( String.format("Los datos para actualizar los campos de '%s' estan vacios"));
        this.entidadNombre=entidadNombre;

        //Registra la excepcion una vez lanzada
        logger.warn("Los datos para actualizar los campos de {}, estan vacios",entidadNombre);
    }

    //getters

    public Logger getLogger() {
        return logger;
    }

    public String getInternalCode() {
        return internalCode;
    }

    public String getEntidadNombre() {
        return entidadNombre;
    }
}
