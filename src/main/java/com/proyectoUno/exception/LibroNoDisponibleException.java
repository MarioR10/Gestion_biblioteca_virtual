package com.proyectoUno.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LibroNoDisponibleException extends RuntimeException {

    //Logger que permite capturar excepciones en consola
   private final Logger logger = LoggerFactory.getLogger(LibroNoDisponibleException.class);

    //ID de la excepcion
   private final String internalCode = "LIBRO_NO_DISPONIBLE";

    //campos que proporciona detalles sobre la excepcion lanzada
    private final String campoBusqueda;
    private final Object valorCampoBusqueda;


    public LibroNoDisponibleException(String campoBusqueda, Object valorCampoBusqueda){

        super(String.format("El Libro no se encuentra disponible en este momento"));
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
}
