package com.proyectoUno.security.dto;

/**
 * DTO que encapsula los datos requeridos
 * para registrar un nuevo usuario en el sistema.
 */
public record RegisterRequest(
        String email,
        String contrasena,
        String nombre,
        String apellido
) {
}
