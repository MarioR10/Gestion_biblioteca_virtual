package com.proyectoUno.maper.usuario;

import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.dto.request.usuario.UsuarioCrearRequestDTO;
import com.proyectoUno.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioRequestMapperStruct {

    Usuario toEntity(UsuarioCrearRequestDTO dto);
    Usuario toEntity(UsuarioActualizarDTO dto);
    List<Usuario> toEntityList( List<UsuarioCrearRequestDTO> dtos);

}
