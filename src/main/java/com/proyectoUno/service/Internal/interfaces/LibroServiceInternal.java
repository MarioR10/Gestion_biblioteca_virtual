package com.proyectoUno.service.Internal.interfaces;

import com.proyectoUno.entity.Libro;

import java.util.UUID;

public interface LibroServiceInternal {

    Libro encontrarLibroPorId(UUID theid);
    void actualizarLibro(Libro libro);
}
