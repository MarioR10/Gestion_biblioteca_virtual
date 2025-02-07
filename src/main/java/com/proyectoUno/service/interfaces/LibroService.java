package com.proyectoUno.service.interfaces;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.dto.request.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.LibroCrearRequestDTO;

import java.util.List;
import java.util.UUID;

public interface LibroService {

    //CRUD para Libro
    List<LibroResponseDTO> encontrarLibros ();
    LibroResponseDTO encontrarLibroPorId (UUID theid);
    void eliminarLibroPorId (UUID theid);
    LibroResponseDTO actualizarLibro(LibroActualizarRequestDTO libroActualizar);
    void guardarLibro(LibroCrearRequestDTO libroCrearRequestDTO);

    //metodos adicionales

    List<LibroResponseDTO> encontrarLibroPorTitulo(String titulo);

    List<LibroResponseDTO> encontrarLibroPorAutor(String autor);

    LibroResponseDTO encontrarLibroPorIsbn(String isbn);

    List<LibroResponseDTO> encontrarLibroPorCategoria(String categoria);

    List<LibroResponseDTO> encontrarLibroPorEstado(String estado);




}
