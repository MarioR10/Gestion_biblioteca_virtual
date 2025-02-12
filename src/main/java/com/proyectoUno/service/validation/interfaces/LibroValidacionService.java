package com.proyectoUno.service.validation.interfaces;

import com.proyectoUno.entity.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroValidacionService {

    void validarListaDeLibrosNoVacia(List<Libro> libros);

    Libro validarLibroExistencia(Optional<Libro> libro);

    void validarDatosActualizacion(Libro datosActualizacion);

    void validarLibroNoDuplicado(Optional <Libro> libro, String isbn);



}
