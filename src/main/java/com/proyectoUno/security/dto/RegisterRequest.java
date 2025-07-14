package com.proyectoUno.security.dto;

//DTO tener los datos necesarios para que un usuario se registre en nuestro sistema
public record RegisterRequest(
        String email,
        String contrasena,
        String nombre,
        String apellido
) {
}
