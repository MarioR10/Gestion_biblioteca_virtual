package com.proyectoUno.dto.request.usuario;

/**
 * DTO para la actualización de un usuario existente.
 * Se utiliza como cuerpo de la solicitud en los endpoints de actualización.
 * Contiene únicamente los campos que se pueden modificar.
 * @param nombre   Nuevo nombre del usuario
 * @param apellido Nuevo apellido del usuario
 * @param email    Nuevo correo electrónico del usuario
 * @param rol      Nuevo rol del usuario en el sistema (por ejemplo, "Admin" o "User")
 * @param activo   Estado de activación del usuario (true si activo, false si desactivado)
 */
public record UsuarioActualizarDTO(

        String nombre,
        String apellido,
        String email,
        String rol,
        boolean activo
) {}


