package com.proyectoUno.dto.request.usuario;

public record UsuarioActualizarDTO(

        String nombre,
        String apellido,
        String email,
        String rol,
        boolean activo
) {}


