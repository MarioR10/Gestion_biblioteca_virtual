package com.proyectoUno.dto.request.usuario;

public record UsuarioCrearRequestDTO(

        String nombre,
        String apellido,
        String email,
        String contraseña,
        String rol


) {}

