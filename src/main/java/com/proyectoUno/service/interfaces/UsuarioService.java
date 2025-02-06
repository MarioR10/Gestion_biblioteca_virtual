package com.proyectoUno.service.interfaces;

import com.proyectoUno.entity.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioService {

    //CRUD para Usuario
     List<Usuario> encontrarUsuarios ();
     Usuario encontrarUsuarioPorId (UUID theid);
     void eliminarUsuarioPorId (UUID theid);
     Usuario actualizarUsuario(Usuario usuario);
     Usuario guardarUsuario(Usuario usuario);

     //Metodos adicionales
     Usuario cambiarRol(UUID theid, String nuevoRol);
     Usuario cambiarEstadoUsuario(UUID theid, boolean estado);
}
