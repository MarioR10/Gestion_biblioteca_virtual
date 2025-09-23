package com.proyectoUno.dto.request.libro;

/**
 * DTO para la creación de un nuevo libro.
 * Se utiliza como cuerpo de la solicitud en los endpoints de creación.
 * @param titulo            Título del libro
 * @param autor             Autor del libro
 * @param categoria         Categoría o género del libro
 * @param isbn              Código ISBN del libro
 * @param anioDePublicacion Año de publicación del libro
 */

public record LibroCrearRequestDTO(
        String titulo,
        String autor,
        String categoria,
        String isbn,
        Integer anioDePublicacion

) {}


