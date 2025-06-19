package com.proyectoUno.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EntidadDuplicadaException extends  RuntimeException{

    //Loggers permiten registrar mensajes en consola, nos ayuda a la depuracion y monitoreo
    private final Logger logger= LoggerFactory.getLogger(EntidadDuplicadaException.class);

    //ID para identificar el tipo de  excepcion lanzada
    private final String internalCode = "ENTIDAD_DUPLICADA";

    //campos que proporciona detalles sobre la excepcion lanzada
    private final String entidadNombre; //Nombre de la entidad que esta duplicada
    private final String campoBusqueda; //Campo con el que se busco la entidad en la DB
    private final List<String> valorCampoBusqueda; // Valor del campo con que se realizo la busqueda en la DB


    /**
     * Constructor prinicipal, inidica que entidad esta duplicada y agrega detalles adicionales
     * @param entidadNombre
     * @param campoBusqueda
     * @param valorCampoBusqueda
     */
    public EntidadDuplicadaException(String entidadNombre, String campoBusqueda,List<String> valorCampoBusqueda){

        super(String.format("Entidad '%s' duplicada, con %s: '%s'",entidadNombre,campoBusqueda,valorCampoBusqueda));
        this.entidadNombre=entidadNombre;
        this.campoBusqueda=campoBusqueda;
        this.valorCampoBusqueda=valorCampoBusqueda;

        logger.warn("Entidad {} duplicada, con {}: {}",entidadNombre,campoBusqueda,valorCampoBusqueda);
    }

    //getter para acceder a los detalles de la excepcion


    public String getInternalCode() {
        return internalCode;
    }

    public String getCampoBusqueda() {
        return campoBusqueda;
    }

    public String getEntidadNombre() {
        return entidadNombre;
    }

    public Object getValorCampoBusqueda() {
        return valorCampoBusqueda;
    }
}





