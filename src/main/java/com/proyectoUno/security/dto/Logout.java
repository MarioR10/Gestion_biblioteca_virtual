package com.proyectoUno.security.dto;

/**
 * DTO que representa la información necesaria
 * para cerrar la sesión de un usuario.
 */
public record Logout(

        String accessToken,
        String refreshToken
) { }
