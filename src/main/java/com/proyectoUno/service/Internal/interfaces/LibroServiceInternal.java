package com.proyectoUno.service.Internal.interfaces;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.entity.Libro;

import java.util.List;
import java.util.UUID;

public interface LibroServiceInternal {

    List<Libro> encontrarLibros ();

    Libro encontrarLibroPorId (UUID theid);
    void eliminarLibroPorId (UUID theid);

    Libro actualizarLibro(UUID id , Libro libro);
    void guardarLibro(List<Libro> libro);

    //metodos adicionales

    List<Libro> encontrarLibroPorTitulo(String titulo);

    List<Libro> encontrarLibroPorAutor(String autor);

    List<Libro> encontrarLibroPorIsbn(String isbn);

    List<Libro> encontrarLibroPorCategoria(String categoria);

    List<Libro> encontrarLibroPorEstado(String estado);

    void marcarLibroComoPrestado(Libro libro);

    void marcarLibroComoDisponible(Libro libro);


}
