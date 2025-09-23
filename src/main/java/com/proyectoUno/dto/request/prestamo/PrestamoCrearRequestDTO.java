package com.proyectoUno.dto.request.prestamo;

import java.util.UUID;

/**
 * DTO para la creación de un nuevo préstamo.
 * Se utiliza como cuerpo de la solicitud en los endpoints de creación de préstamos.
 *
 * @param idUsuario UUID del usuario que solicita el préstamo
 * @param idLibro   UUID del libro que se va a prestar
 */
public record PrestamoCrearRequestDTO (
        UUID idUsuario,
        UUID idLibro
){}
