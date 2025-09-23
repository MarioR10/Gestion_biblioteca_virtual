package com.proyectoUno.dto.request.libro;


/**
 * DTO para la actualización de un libro.
 * Se utiliza como cuerpo de la solicitud en los endpoints de actualización.
 * Contiene únicamente los campos que se pueden modificar.
 * @param titulo            Nuevo título del libro
 * @param autor             Nuevo autor del libro
 * @param categoria         Nueva categoría o género del libro
 * @param anioDePublicacion Nuevo año de publicación del libro
 */
public record LibroActualizarRequestDTO (
        String titulo,
        String autor,
        String categoria,
        Integer anioDePublicacion

){}



