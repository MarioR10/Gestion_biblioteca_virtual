package com.proyectoUno.dto.request.libro;


public record LibroCrearRequestDTO(
        String titulo,
        String autor,
        String categoria,
        String isbn,
        Integer anioDePublicacion

) {}


