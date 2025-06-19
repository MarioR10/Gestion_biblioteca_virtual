package com.proyectoUno.service.interfaces;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LibroService {

    LibroResponseDTO encontrarLibroPorId(UUID id);

    LibroResponseDTO actualizarLibro(UUID id, LibroActualizarRequestDTO libroActualizar);

    void crearLibro(List<LibroCrearRequestDTO> libroCrearRequestDTO);

    Page<LibroResponseDTO> encontrarLibros(Pageable pageable);

    Page<LibroResponseDTO> encontrarLibroPorTitulo(String titulo, Pageable pageable);

    Page<LibroResponseDTO> encontrarLibroPorAutor(String autor, Pageable pageable);

    Page<LibroResponseDTO> encontrarLibroPorIsbn(String isbn, Pageable pageable);

    Page<LibroResponseDTO> encontrarLibroPorCategoria(String categoria, Pageable pageable);

    Page<LibroResponseDTO> encontrarLibroPorEstado(String estado, Pageable pageable);


    //metodos actuales
    Libro encontrarLibroPorIdInternal(UUID theid);

    void eliminarLibroPorId(UUID theid);

    void marcarLibroComoPrestado(Libro libro);

    void marcarLibroComoDisponible(Libro libro);

}