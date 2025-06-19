package com.proyectoUno.service.interfaces;

import com.proyectoUno.dto.reponse.UsuarioResponseDTO;
import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.dto.request.usuario.UsuarioCrearRequestDTO;
import com.proyectoUno.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UsuarioService {



   UsuarioResponseDTO encontrarUsuarioPorId(UUID id);
   UsuarioResponseDTO actualizarUsuario(UUID id, UsuarioActualizarDTO usuarioActualizar);
   void crearUsuario(List<UsuarioCrearRequestDTO> usuariosDTO);
   void eliminarUsuarioPorId(UUID id);

   //metodo paginado
   Page<UsuarioResponseDTO> encontrarUsuarios(Pageable pageable);

   //metodos internos
   Usuario encontrarUsuarioPorIdInterno(UUID id);


}
