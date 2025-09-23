package com.proyectoUno.dto.reponse;

import java.util.UUID;

/**
 * DTO para representar la información de un libro.
 * Contiene los datos básicos que se exponen al cliente.
 * @param id               UUID único del libro
 * @param titulo           Título del libro
 * @param autor            Autor del libro
 * @param isbn             Código ISBN del libro
 * @param categoria        Categoría o género del libro
 * @param anioDePublicacion Año de publicación del libro
 * @param estado           Estado del libro en la base de datos (por ejemplo, "activo" o "prestado")
 */
public record LibroResponseDTO (

        UUID id,
        String titulo,
        String autor,
        String isbn,
        String categoria,
        Integer anioDePublicacion,
        String estado

        /*
         * Nota: Si los nombres que vienen en el JSON no coinciden con los del record,
         * se puede usar @JsonProperty para mapearlos:
         * @JsonProperty("nombre_libro") String titulo;
         */
){}







