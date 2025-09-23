package com.proyectoUno.dto.reponse;

import java.util.UUID;

/**
 * DTO para representar la información de un usuario.
 * Contiene los datos básicos que se exponen al cliente.
 * @param id      UUID único del usuario
 * @param nombre  Nombre del usuario
 * @param apellido Apellido del usuario
 * @param email   Correo electrónico del usuario
 * @param rol     Rol del usuario en el sistema (por ejemplo, "Admin" o "User")
 */
public record UsuarioResponseDTO(

         UUID id,
         String nombre,
         String apellido,
         String email,
         String rol
        /*
         * Nota: Si los nombres que vienen en el JSON no coinciden con los del record,
         * se puede usar @JsonProperty para mapearlos:
         * @JsonProperty("nombre_libro") String titulo;
         */
) {}
