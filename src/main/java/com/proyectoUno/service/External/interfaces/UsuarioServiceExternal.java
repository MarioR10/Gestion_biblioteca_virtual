package com.proyectoUno.service.External.interfaces;

import com.proyectoUno.dto.reponse.UsuarioResponseDTO;
import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.dto.request.usuario.UsuarioCrearRequestDTO;
import com.proyectoUno.entity.Usuario;

import java.util.List;
import java.util.UUID;

public interface UsuarioServiceExternal {

    //CRUD para Usuario
     List<UsuarioResponseDTO> encontrarUsuarios ();
     UsuarioResponseDTO encontrarUsuarioPorId (UUID theid);
     void eliminarUsuarioPorId (UUID theid);
     UsuarioResponseDTO actualizarUsuario(UUID id, UsuarioActualizarDTO usuarioActualizar);
     void guardarUsuario(UsuarioCrearRequestDTO usuarioDTO);

}


