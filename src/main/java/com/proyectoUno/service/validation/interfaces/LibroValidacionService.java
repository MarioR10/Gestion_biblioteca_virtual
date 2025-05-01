package com.proyectoUno.service.validation.interfaces;

import com.proyectoUno.entity.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroValidacionService {

    void validarDatosActualizacion(Libro datosActualizacion);

    void validarDisponibilidadDelLibro(Libro libro);


}
