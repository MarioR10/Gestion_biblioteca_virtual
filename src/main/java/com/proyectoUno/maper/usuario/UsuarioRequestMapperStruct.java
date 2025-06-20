package com.proyectoUno.maper.usuario;

import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.dto.request.usuario.UsuarioCrearRequestDTO;
import com.proyectoUno.entity.Usuario;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioRequestMapperStruct {

    Usuario toEntity(UsuarioCrearRequestDTO dto);
    List<Usuario> toEntityList( List<UsuarioCrearRequestDTO> dtos);

    //Metodo para mapear DTO -> Entidad existente
    //La primera anotacion le indica a MapStruct que si un campo del DTO es null, no la incluya al mapeo de la entidad
    //La segunda anotacion le indica a MapStruct que no cree una nueva instancia (Entidad), si no que se enfoque (Target) en la que le pasamos
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateEntity(UsuarioActualizarDTO dto, @MappingTarget Usuario usuario);

}
