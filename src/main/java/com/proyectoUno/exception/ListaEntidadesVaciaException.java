package com.proyectoUno.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ListaEntidadesVaciaException extends  RuntimeException {

    //Loggers permiten registrar mensajes en consola, nos ayuda a la depuracion y monitoreo
    private final Logger logger = LoggerFactory.getLogger(ListaEntidadesVaciaException.class);

    //ID para identificar el tipo de excepcion
    private final String internalCode = "LISTA_ENTIDADES_VACIA";

    //Campos que proporcionan detalles sobre la excepcion lanzada
    private final String entidadNombre; //Nombre de la entidad cuya lista esta vacia


    /**
     * Constructor prinicipal, inidica que lista de entidades esta vacia con un mensaje predeterminado
     * @param entidadNombre
     */
    public ListaEntidadesVaciaException(String entidadNombre){
        super(String.format("La lista de entidades '%s' esta vacia"));
        this.entidadNombre=entidadNombre;

        //Registra la excepcion como advertencia  en los logs una vez creada.
        logger.warn("Lista de entidades vacia para: {}", entidadNombre);
    }

    //  Getters para acceder a los detalles de la excepción

    public String getInternalCode(){return internalCode;}
    public String getEntidadNombre(){return entidadNombre;}



}
