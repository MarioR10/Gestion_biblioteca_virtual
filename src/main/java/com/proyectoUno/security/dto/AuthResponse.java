package com.proyectoUno.security.dto;

/**
 * DTO de respuesta que devuelve los tokens generados
 * después de un proceso de autenticación.
 */
public record AuthResponse( String accessToken ,String refreshToken){
}
