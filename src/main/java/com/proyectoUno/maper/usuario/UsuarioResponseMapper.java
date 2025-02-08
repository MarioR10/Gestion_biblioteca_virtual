package com.proyectoUno.maper.usuario;

import com.proyectoUno.dto.reponse.UsuarioResponseDTO;
import com.proyectoUno.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsuarioResponseMapper {


    //Metodo para convertir una entidad a un DTO Response

    public UsuarioResponseDTO convertirAResponseDTO(Usuario usuario){

        //DTO
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();

        //Entidad a DTO
        usuarioResponseDTO.setNombre(usuario.getNombre());
        usuarioResponseDTO.setApellido(usuario.getApellido());
        usuarioResponseDTO.setEmail(usuario.getEmail());
        usuarioResponseDTO.setRol(usuario.getRol());

        //Retonarmos DTO
        return usuarioResponseDTO;


    }

    public List<UsuarioResponseDTO> convertirAListaResponseDTO(List<Usuario> usuarios){


        return usuarios.stream()
                .map(this::convertirAResponseDTO)// Convierte cada usuario a un DTO (nuestro metodo anterior)
                .collect(Collectors.toList()); //  Recolecta los resultados en una lista ahora de tipo DTO
    }

}
