package com.proyectoUno.mapper.usuario;

import com.proyectoUno.dto.reponse.UsuarioResponseDTO;
import com.proyectoUno.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioResponseMapperStruct {

  UsuarioResponseDTO toResponseDTO( Usuario usuario);

  default Page<UsuarioResponseDTO> toResponsePageDTO(Page<Usuario> usuarios){

      List<UsuarioResponseDTO> dtoList = usuarios.getContent()
              .stream().map(this::toResponseDTO)
              .collect(Collectors.toList());
      return new PageImpl <>(dtoList, usuarios.getPageable(), usuarios.getTotalElements());
  }
}
