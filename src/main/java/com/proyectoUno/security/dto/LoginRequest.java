package com.proyectoUno.security.dto;

//DTO que encapsula las credenciales del usuasrio, utilizada para iniciar sesion
public record LoginRequest(
        String email,
        String contrasena
) {}
