package com.proyectoUno.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excepción personalizada que se lanza cuando ocurre un conflicto
 * de datos en la aplicación, por ejemplo, cuando un registro ya existe.
 */
public class ConflictException extends RuntimeException {

    /** Logger para capturar detalles de la excepción en consola */
   private final Logger logger = LoggerFactory.getLogger(ConflictException.class);

    /** Código interno de la excepción */
   private final String internalCode = "CONFLICTO";

    /** Campos que proporciona detalles sobre la excepcion lanzada*/
    private final String entidadNombre;
    private final String campoBusqueda;
    private final Object valorCampoBusqueda;


    /**
     * Constructor principal de la excepción.
     * @param message         Mensaje descriptivo de la excepción
     * @param entidadNombre   Nombre de la entidad donde ocurrió el conflicto
     * @param campoBusqueda   Nombre del campo que causó el conflicto
     * @param valorCampoBusqueda Valor del campo conflictivo
     */
    public ConflictException(String message, String entidadNombre, String campoBusqueda, Object valorCampoBusqueda){

        super(message);
        this.entidadNombre=entidadNombre;
        this.campoBusqueda=campoBusqueda;
        this.valorCampoBusqueda=valorCampoBusqueda;
    }

    public ConflictException(String message){
        super(message);
        this.entidadNombre=null;
        this.campoBusqueda= null;
        this.valorCampoBusqueda=null;
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
