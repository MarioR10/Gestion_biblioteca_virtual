package com.proyectoUno.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excepción que se lanza cuando se intenta actualizar una entidad
 * pero los datos proporcionados están vacíos o no son válidos.
 */
public class SolicitudActualizacionInvalidaException extends  RuntimeException {

    /**logger que permite capturar las excepciones en consola*/
    private final Logger logger = LoggerFactory.getLogger(SolicitudActualizacionInvalidaException.class);

    /** Identificador interno de la excepción*/
    private final String internalCode = "SOLICITUD_DE_ACTUALIZACION_INVALIDA";

    /** Nombre de la entidad que se intentó actualizar*/
    private final String entidadNombre;

    /**
     * Constructor principal.
     * Genera un mensaje predeterminado indicando que los datos para actualizar están vacíos.
     * @param entidadNombre nombre de la entidad que se intentó actualizar
     */
    public SolicitudActualizacionInvalidaException( String entidadNombre){
        super( String.format("Los datos para actualizar los campos de '%s' estan vacios"));
        this.entidadNombre=entidadNombre;

        //Registra la excepcion una vez lanzada
        logger.warn("Los datos para actualizar los campos de {}, estan vacios",entidadNombre);
    }

    //getters

    public String getInternalCode() {
        return internalCode;
    }

    public String getEntidadNombre() {
        return entidadNombre;
    }
}
