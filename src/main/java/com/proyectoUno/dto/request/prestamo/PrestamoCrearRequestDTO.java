package com.proyectoUno.dto.request.prestamo;

import java.util.UUID;

public record PrestamoCrearRequestDTO (
        UUID idUsuario,
        UUID idLibro
){}
