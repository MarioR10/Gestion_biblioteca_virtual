package com.proyectoUno.maper.usuario;

import com.proyectoUno.dto.reponse.UsuarioResponseDTO;
import com.proyectoUno.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper de MapStruct para convertir entidades Usuario a sus DTOs de respuesta (UsuarioResponseDTO).
 * Permite:
 * 1. Mapear un solo Usuario a su DTO de respuesta.
 * 2. Mapear una página de entidades Usuario a una página de DTOs de respuesta,
 *    conservando la información de paginación.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioResponseMapperStruct {

    /**
     * Convierte una entidad Usuario a su DTO de respuesta.
     * @param usuario Entidad Usuario a convertir
     * @return DTO de respuesta con los datos del usuario
     */
  UsuarioResponseDTO toResponseDTO( Usuario usuario);

    /**
     * Convierte una página de entidades Usuario a una página de DTOs de respuesta.
     * Este método:
     * - Mantiene la información de paginación (pageable y totalElements)
     * - Convierte cada elemento de la página usando el método toResponseDTO
     * @param usuarios Página de entidades Usuario
     * @return Página de DTOs de respuesta
     */
  default Page<UsuarioResponseDTO> toResponsePageDTO(Page<Usuario> usuarios){

      List<UsuarioResponseDTO> dtoList = usuarios.getContent()
              .stream().map(this::toResponseDTO)
              .collect(Collectors.toList());
      return new PageImpl <>(dtoList, usuarios.getPageable(), usuarios.getTotalElements());
  }
}
