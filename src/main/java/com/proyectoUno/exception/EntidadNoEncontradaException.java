package com.proyectoUno.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Excepción personalizada que se lanza cuando no se encuentra
 * una entidad en la base de datos.
 */
public class EntidadNoEncontradaException extends  RuntimeException{

    /** Logger para depuración y monitoreo */
    private static final Logger logger = LoggerFactory.getLogger(EntidadNoEncontradaException.class);

    /** Código interno de la excepción */
    private final String internalCode = "ENTITY_NOT_FOUND";

    /**Campos que proporcionan detalles sobre la excepcion lanzada*/
    private final String entidadNombre; // Nombre de la entidad que se intento encontrar en la DB.
    private final String campoBusqueda; // Campo con el que se trato de encotnrar la entidad en la DB.
    private final Object valorCampoBusqueda; //Valor del campo con que se trato de encontrar la entidad en la DB.

    /**
     * Constructor principal.
     * @param entidadNombre Nombre de la entidad buscada
     * @param campoBusqueda Campo usado para buscar la entidad
     * @param valorCampoBusqueda Valor del campo que provocó la búsqueda fallida
     */
    public EntidadNoEncontradaException(String entidadNombre, String campoBusqueda, Object valorCampoBusqueda){
        super(String.format("La entidad '%s' no fue encontrada con %s: '%s'",entidadNombre,campoBusqueda,valorCampoBusqueda));
        this.entidadNombre=entidadNombre;
        this.campoBusqueda=campoBusqueda;
        this.valorCampoBusqueda=valorCampoBusqueda;

        //Registra la excepcion como advertencia  en los logs una vez creada.
        logger.warn("Entidad no encontrada: {} con {}: {}", entidadNombre,campoBusqueda,valorCampoBusqueda);
    }

    /**
     * Constructor secundario con mensaje personalizado.
     * @param message Mensaje de error.
     */
    public EntidadNoEncontradaException(String message){
        super(message);
        this.entidadNombre=null;
        this.campoBusqueda= null;
        this.valorCampoBusqueda=null;
        //Registra la excepcion como advertencia  en los logs una vez creada.
        logger.warn("Entidad no encontrada: {}",message);

    }

    //Metodos gett para acceder a los detalles de la excepcion

    public String getInternalCode(){return internalCode;}
    public String getEntidadNombre() {return entidadNombre;}
    public String getCampoBusqueda() {return campoBusqueda;}
    public Object getValorCampoBusqueda() {return valorCampoBusqueda;}

}
