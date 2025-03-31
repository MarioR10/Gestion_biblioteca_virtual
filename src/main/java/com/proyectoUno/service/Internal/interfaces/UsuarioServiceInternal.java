package com.proyectoUno.service.Internal.interfaces;

import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadNoEncontradaException;

import java.util.List;
import java.util.UUID;

public interface UsuarioServiceInternal {

   List<Usuario> encontrarUsuarios();

   Usuario encontrarUsuarioPorId(UUID id);

   void eliminarUsuarioPorId(UUID id);

   Usuario actualizarUsuario(UUID id,Usuario usuario );

   void guardarUsuario(List<Usuario> usuarios);

   Usuario encontrarUsuarioPorEmail(String email);




}
