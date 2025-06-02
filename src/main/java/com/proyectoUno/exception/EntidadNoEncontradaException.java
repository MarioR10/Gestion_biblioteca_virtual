package com.proyectoUno.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;


public class EntidadNoEncontradaException extends  RuntimeException{

  //Loggers permiten registrar mensajes en consola, nos ayuda a la depuracion y monitoreo
    private static final Logger logger = LoggerFactory.getLogger(EntidadNoEncontradaException.class);

    //ID para identificar el tipo de excepcion (internalCode)
    private final String internalCode = "ENTITY_NOT_FOUND";

    //Campos que proporcionan detalles sobre la excepcion lanzada
    private final String entidadNombre; // Nombre de la entidad que se intento encontrar en la DB.
    private final String campoBusqueda; // Campo con el que se trato de encotnrar la entidad en la DB.
    private final Object valorCampoBusqueda; //Valor del campo con que se trato de encontrar la entidad en la DB.

    /**
     * Constructor prinicipal, para crear una excepcion cuando la entidad no ha sido encontrada mediante un campo.
     * Genera un mensaje de error predeterminado, con los valores obtenidos.
     * @param entidadNombre
     * @param campoBusqueda
     * @param valorCampoBusqueda
     */

    public EntidadNoEncontradaException(String entidadNombre, String campoBusqueda, String valorCampoBusqueda){
        super(String.format("La entidad '%s' no fue encontrada con %s: '%s'",entidadNombre,campoBusqueda,valorCampoBusqueda));
        this.entidadNombre=entidadNombre;
        this.campoBusqueda=campoBusqueda;
        this.valorCampoBusqueda=valorCampoBusqueda;

        //Registra la excepcion como advertencia  en los logs una vez creada.
        logger.warn("Entidad no encontrada: {} con {}: {}", entidadNombre,campoBusqueda,valorCampoBusqueda);
    }

    /**
     * Constructor secundario que permite crear la excepcion, unicamente con un mensaje personalizado.
     * Util para cuando no se conocen detalles especificos.
     *
     * @param message
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
