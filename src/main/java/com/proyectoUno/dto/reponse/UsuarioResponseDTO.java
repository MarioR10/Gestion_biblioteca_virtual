package com.proyectoUno.dto.reponse;

import java.util.UUID;

public record UsuarioResponseDTO(

         UUID id,
         String nombre,
         String apellido,
         String email,
         String rol



) {}
