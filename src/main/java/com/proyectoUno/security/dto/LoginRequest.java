package com.proyectoUno.security.dto;

/**
 * DTO que representa las credenciales del usuario
 * utilizadas en el inicio de sesión.
 */
public record LoginRequest(
        String email,
        String contrasena
) {}
