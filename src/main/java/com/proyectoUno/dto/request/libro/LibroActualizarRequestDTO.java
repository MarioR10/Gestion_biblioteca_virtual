package com.proyectoUno.dto.request.libro;


public record LibroActualizarRequestDTO (
        String titulo,
        String autor,
        String categoria,
        Integer anioDePublicacion

){}



