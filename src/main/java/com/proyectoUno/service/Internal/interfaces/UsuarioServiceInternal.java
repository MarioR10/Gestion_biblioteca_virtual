package com.proyectoUno.service.Internal.interfaces;

import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadNoEncontradaException;

import java.util.UUID;

public interface UsuarioServiceInternal {

    Usuario encontrarUsuarioPorId (UUID theid);





}
