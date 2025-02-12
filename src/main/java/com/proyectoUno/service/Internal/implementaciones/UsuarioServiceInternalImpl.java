package com.proyectoUno.service.Internal.implementaciones;

import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioServiceInternalImpl implements com.proyectoUno.service.Internal.interfaces.UsuarioServiceInternal {


    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceInternalImpl(UsuarioRepository usuarioRepository){

        this.usuarioRepository=usuarioRepository;
    }

    @Override
    public Usuario  encontrarUsuarioPorId(UUID theid) {

        //Verificamos que se encuentre la entidad dentro del Optional, si no esta tiramos excepcion
        return  usuarioRepository.findById(theid)
                .orElseThrow(()-> new EntidadNoEncontradaException("El Usuario con ID: "+theid+ " no ha sido encontrado"));

    }

}
