package com.proyectoUno.maper.usuario;

import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.dto.request.usuario.UsuarioCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioRequestMapper {

    // Metodo para actualizar una entidad a partir de un DTO request

    public Usuario actualizarEntidadDesdeDTO(UsuarioActualizarDTO usuarioActualizarDTO){

        Usuario usuarioActualizar = new Usuario();

        usuarioActualizar.setNombre(usuarioActualizarDTO.getNombre());
        usuarioActualizar.setApellido(usuarioActualizarDTO.getApellido());
        usuarioActualizar.setEmail(usuarioActualizarDTO.getEmail());
        usuarioActualizar.setRol(usuarioActualizarDTO.getRol());
        usuarioActualizar.setActivo(usuarioActualizarDTO.getActivo());


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

    //Metodo para convertir a lista de Entidades
    public List<Usuario> convertirAListaEntidad(List<UsuarioCrearRequestDTO> usuarios){

        return usuarios.stream()
                .map(this::crearEntidadDesdeDTO)// Convierte cada Uusario a un DTO (nuestro metodo anterior)
                .collect(Collectors.toList()); //  Recolecta los resultados en una lista ahora de tipo Entidad
    }




}
