package com.proyectoUno.security.dto;

/**
 * DTO que contiene el token de refresco,
 * utilizado para solicitar un nuevo access token.
 */
public record RefreshTokenRequest(
        String refreshToken
) {
}
