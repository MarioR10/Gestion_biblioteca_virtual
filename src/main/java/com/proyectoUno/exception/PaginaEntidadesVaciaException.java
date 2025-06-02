package com.proyectoUno.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class PaginaEntidadesVaciaException extends  RuntimeException{

    //Logger, Permite registrar mensajes en consola, ayuda con el debbug y monitoreo.
    private final Logger logger = LoggerFactory.getLogger(PaginaEntidadesVaciaException.class);

    //ID para identificar el tipo de excepcion
    private final String internalCode= "PAGINA_ENTIDADES_VACIA";
    //Campos que proporcionan detalles sobre la excepcion lanzada
    private final String entidadNombre; //Nombre de la entidad cuya lista esta vacia


    public PaginaEntidadesVaciaException(String entidadNombre){
        super(String.format("Pagina de entidades '%s' esta vacia"));
        this.entidadNombre=entidadNombre;

        //Registra la excepcion como advertencia  en los logs una vez creada.
        logger.warn("Pagina de entidades vacia para: {}", entidadNombre);
    }

    //  Getters para acceder a los detalles de la excepción

    public String getInternalCode(){return internalCode;}
    public String getEntidadNombre(){return entidadNombre;}




}
