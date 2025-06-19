package com.proyectoUno.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConflictException extends RuntimeException {

    //Logger que permite capturar excepciones en consola
   private final Logger logger = LoggerFactory.getLogger(ConflictException.class);

    //ID de la excepcion
   private final String internalCode = "CONFLICTO";

    //campos que proporciona detalles sobre la excepcion lanzada
    private final String entidadNombre;
    private final String campoBusqueda;
    private final Object valorCampoBusqueda;


    public ConflictException(String message, String entidadNombre, String campoBusqueda, Object valorCampoBusqueda){

        super(message);
        this.entidadNombre=entidadNombre;
        this.campoBusqueda=campoBusqueda;
        this.valorCampoBusqueda=valorCampoBusqueda;
    }

    //Getters
    public String getInternalCode() {
        return internalCode;
    }

    public String getCampoBusqueda() {
        return campoBusqueda;
    }

    public Object getValorCampoBusqueda() {
        return valorCampoBusqueda;
    }

    public String getEntidadNombre() {
        return entidadNombre;
    }
}
