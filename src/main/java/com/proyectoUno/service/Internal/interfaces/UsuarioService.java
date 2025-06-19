package com.proyectoUno.service.Internal.interfaces;

import com.proyectoUno.dto.reponse.UsuarioResponseDTO;
import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.dto.request.usuario.UsuarioCrearRequestDTO;
import com.proyectoUno.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UsuarioServiceInternal {


   //Metodos que retonan DTO
   UsuarioResponseDTO encontrarUsuarioPorId(UUID id);
   UsuarioResponseDTO actualizarUsuario(UUID id, UsuarioActualizarDTO usuarioActualizar);
   void crearUsuario(List<UsuarioCrearRequestDTO> usuariosDTO);




   //Metodos actuales

   Usuario encontrarUsuarioPorIdInterno(UUID id);
   void eliminarUsuarioPorId(UUID id);
   Usuario actualizarUsuario(UUID id,Usuario usuario );
   void crearUsuarioInterno(List<Usuario> usuarios);
   Usuario encontrarUsuarioPorEmail(String email);

//metodo paginado
   Page<UsuarioResponseDTO> encontrarUsuarios(Pageable pageable);


}
