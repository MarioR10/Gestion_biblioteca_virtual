package com.proyectoUno.maper.usuario;

import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.dto.request.usuario.UsuarioCrearRequestDTO;
import com.proyectoUno.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioRequestMapper {

    // Metodo para actualizar una entidad a partir de un DTO request

    public Usuario actualizarEntidadDesdeDTO(UsuarioActualizarDTO usuarioActualizarDTO){

        Usuario usuarioActualizar = new Usuario();

        usuarioActualizar.setEmail(usuarioActualizarDTO.getEmail());
        usuarioActualizar.setRol(usuarioActualizarDTO.getRol());


        return  usuarioActualizar;
    }


    //Metodo para crear una entidad a partir de un DTO request

    public Usuario crearEntidadDesdeDTO (UsuarioCrearRequestDTO usuarioCrearRequestDTO){

        Usuario usuario= new Usuario();

        if (usuarioCrearRequestDTO.getNombre() != null){
            usuario.setNombre(usuarioCrearRequestDTO.getNombre());
        }

        if (usuarioCrearRequestDTO.getApellido() != null){
            usuario.setApellido(usuarioCrearRequestDTO.getApellido());
        }

        if (usuarioCrearRequestDTO.getEmail() != null){
            usuario.setEmail(usuarioCrearRequestDTO.getEmail());
        }

        if (usuarioCrearRequestDTO.getContraseña() != null){
            usuario.setContrasena(usuarioCrearRequestDTO.getContraseña());
        }

        if (usuarioCrearRequestDTO.getRol() != null){
            usuario.setRol(usuarioCrearRequestDTO.getRol());
        }

        return usuario;

    }
}
