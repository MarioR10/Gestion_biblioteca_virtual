package com.proyectoUno.dto.reponse;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para representar la información de un préstamo.
 * Contiene los datos básicos que se exponen al cliente sobre un préstamo específico.
 * @param id               UUID único del préstamo
 * @param fechaPrestamo    Fecha y hora en que se realizó el préstamo
 * @param fechaDevolucion  Fecha y hora de devolución del libro (puede ser null si aún no se ha devuelto)
 * @param estado           Estado del préstamo (por ejemplo, "activo", "devuelto" o "vencido")
 * @param usuario          DTO con la información del usuario que realizó el préstamo
 * @param libro            DTO con la información del libro prestado
 */
public record PrestamoResponseDTO(

        UUID id,
        LocalDateTime fechaPrestamo,
        LocalDateTime fechaDevolucion,
        String estado,
        UsuarioResponseDTO usuario,
        LibroResponseDTO libro

        /*
         * Nota: Si los nombres que vienen en el JSON no coinciden con los del record,
         * se puede usar @JsonProperty para mapearlos:
         * @JsonProperty("nombre_libro") String titulo;
         */
) { }
