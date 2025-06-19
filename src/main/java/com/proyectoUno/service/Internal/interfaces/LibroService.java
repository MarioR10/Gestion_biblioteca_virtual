package com.proyectoUno.service.Internal.interfaces;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.entity.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LibroServiceInternal {

    LibroResponseDTO encontrarLibroPorId(UUID id);
    LibroResponseDTO actualizarLibro(UUID id, LibroActualizarRequestDTO libroActualizar);





    //metodos actuales
    Libro encontrarLibroPorIdInternal(UUID theid);
    void eliminarLibroPorId (UUID theid);
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
