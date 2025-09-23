package com.proyectoUno.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Excepción personalizada que se lanza cuando se detecta
 * que una entidad ya existe en la base de datos (duplicada).
 */
public class EntidadDuplicadaException extends  RuntimeException{

    /** Logger para depuración y monitoreo */
    private final Logger logger= LoggerFactory.getLogger(EntidadDuplicadaException.class);

    /** Código interno de la excepción */
    private final String internalCode = "ENTIDAD_DUPLICADA";

    /**Campos que proporciona detalles sobre la excepcion lanzada*/
    private final String entidadNombre; //Nombre de la entidad que esta duplicada
    private final String campoBusqueda; //Campo con el que se busco la entidad en la DB
    private final List<String> valorCampoBusqueda; // Valor del campo con que se realizo la busqueda en la DB


    /**
     * Constructor principal.
     * @param entidadNombre Nombre de la entidad duplicada
     * @param campoBusqueda Campo usado para la búsqueda
     * @param valorCampoBusqueda Valor del campo que causó la duplicación
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





