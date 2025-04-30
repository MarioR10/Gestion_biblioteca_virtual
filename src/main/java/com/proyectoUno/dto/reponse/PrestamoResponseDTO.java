package com.proyectoUno.dto.reponse;

import java.time.LocalDateTime;
import java.util.UUID;

public record PrestamoResponseDTO(
        UUID id,
        LocalDateTime fechaPrestamo,
        LocalDateTime fechaDevolucion,
        String estado,
        UsuarioResponseDTO usuario,
        LibroResponseDTO libro

        ) { }
