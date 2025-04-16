package com.proyectoUno.service.Internal.interfaces;

import com.proyectoUno.entity.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LibroServiceInternal {

    //metodos actuales
    Libro encontrarLibroPorId (UUID theid);
    void eliminarLibroPorId (UUID theid);
    Libro actualizarLibro(UUID id , Libro libro);
    void crearLibro(List<Libro> libro);
    void marcarLibroComoPrestado(Libro libro);
    void marcarLibroComoDisponible(Libro libro);


    Page<Libro> encontrarLibros (Pageable pageable);
    Page<Libro> encontrarLibroPorTitulo(String titulo, Pageable pageable);
    Page<Libro> encontrarLibroPorAutor(String autor,Pageable pageable);
    Page<Libro> encontrarLibroPorIsbn(String isbn, Pageable pageable);
    Page<Libro> encontrarLibroPorCategoria(String categoria,Pageable pageable);
    Page<Libro> encontrarLibroPorEstado(String estado, Pageable pageable);


}
