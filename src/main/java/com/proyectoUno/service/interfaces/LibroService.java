package com.proyectoUno.service.interfaces;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LibroService {

    //CRUD para Libro
    List<Libro> encontrarLibros ();
    Libro encontrarLibroPorId (UUID theid);
    void eliminarLibroPorId (UUID theid);
    Libro actualizarLibro(Libro libro);
    Libro guardarLibro(Libro libro);

    //metodos adicionales

    List<Libro> encontrarLibroPorTitulo(String titulo);

    List<Libro> encontrarLibroPorAutor(String autor);

    Libro encontrarLibroPorIsbn(String isbn);

    List<Libro> encontrarLibroPorCategoria(String categoria);

    List<Libro> encontrarLibroPorEstado(String estado);




}
