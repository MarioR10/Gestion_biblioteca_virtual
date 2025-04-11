package com.proyectoUno.service.Internal.interfaces;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
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
    void guardarLibro(List<Libro> libro);
    void marcarLibroComoPrestado(Libro libro);
    void marcarLibroComoDisponible(Libro libro);


    //metodos atuales sin paginacion
    List<Libro> encontrarLibros ();
    List<Libro> encontrarLibroPorTitulo(String titulo);
    List<Libro> encontrarLibroPorAutor(String autor);
    List<Libro> encontrarLibroPorIsbn(String isbn);
    List<Libro> encontrarLibroPorCategoria(String categoria);
    List<Libro> encontrarLibroPorEstado(String estado);

    //metodos paginados

    Page<Libro> encontrarLibros (Pageable pageable);
    Page<Libro> encontrarLibroPorTitulo(String titulo, Pageable pageable);
    Page<Libro> encontrarLibroPorAutor(String autor,Pageable pageable);
    Page<Libro> encontrarLibroPorIsbn(String isbn, Pageable pageable);
    Page<Libro> encontrarLibroPorCategoria(String categoria,Pageable pageable);
    Page<Libro> encontrarLibroPorEstado(String estado, Pageable pageable);


}
