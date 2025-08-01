package com.proyectoUno.security.dto;

public record Logout(

        String accessToken,
        String refreshToken
) { }
