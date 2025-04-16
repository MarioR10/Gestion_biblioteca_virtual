package com.proyectoUno.service.Internal.interfaces;

import com.proyectoUno.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UsuarioServiceInternal {

   //Metodos actuales

   Usuario encontrarUsuarioPorId(UUID id);
   void eliminarUsuarioPorId(UUID id);
   Usuario actualizarUsuario(UUID id,Usuario usuario );
   void crearUsuario(List<Usuario> usuarios);
   Usuario encontrarUsuarioPorEmail(String email);

//metodo paginado
   Page<Usuario> encontrarUsuarios(Pageable pageable);


}
