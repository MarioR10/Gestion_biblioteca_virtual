package com.proyectoUno.dto.request.usuario;

/**
 * DTO para la creación de un nuevo usuario.
 * Se utiliza como cuerpo de la solicitud en los endpoints de creación de usuarios.
 * @param nombre     Nombre del usuario
 * @param apellido   Apellido del usuario
 * @param email      Correo electrónico del usuario
 * @param contrasena Contraseña del usuario
 * @param rol        Rol del usuario en el sistema (por ejemplo, "Admin" o "User")
 */
public record UsuarioCrearRequestDTO(

        String nombre,
        String apellido,
        String email,
        String contrasena,
        String rol


) {}

