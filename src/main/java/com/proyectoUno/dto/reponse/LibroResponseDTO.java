package com.proyectoUno.dto.reponse;

import java.util.UUID;

public record LibroResponseDTO (

        UUID id,
        String titulo,
        String autor,
        String isbn,
        String categoria,
        Integer anioDePublicacion,
        String estado

        /* Si los nombres que vienen en el JSON no coinciden con los del record, debemos anotarlos similar a:
         @JsonProperty("nombre_libro") string titulo; donde nombre_libro es como viene en el JSON
        */
){}







