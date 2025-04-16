package com.proyectoUno.service.External.interfaces;

import com.proyectoUno.dto.reponse.UsuarioResponseDTO;
import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.dto.request.usuario.UsuarioCrearRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UsuarioServiceExternal {

    //CRUD para Usuario
     List<UsuarioResponseDTO> encontrarUsuarios ();
     UsuarioResponseDTO encontrarUsuarioPorId (UUID theid);
     void eliminarUsuarioPorId (UUID theid);
     UsuarioResponseDTO actualizarUsuario(UUID id, UsuarioActualizarDTO usuarioActualizar);
     void crearUsuario(List<UsuarioCrearRequestDTO> usuarioDTO);


     //metodo paginado
     Page<UsuarioResponseDTO> encontrarUsuarios(Pageable pageable);
}


