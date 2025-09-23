package com.proyectoUno.maper.usuario;

import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.dto.request.usuario.UsuarioCrearRequestDTO;
import com.proyectoUno.entity.Usuario;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper de MapStruct para convertir entre la entidad Usuario y sus DTOs de request.
 * Permite:
 * 1. Mapear un DTO de creación (UsuarioCrearRequestDTO) a la entidad Usuario.
 * 2. Mapear una lista de DTOs de creación a una lista de entidades Usuario.
 * 3. Actualizar una entidad existente a partir de un DTO de actualización (UsuarioActualizarDTO),
 *    ignorando los campos que sean null en el DTO.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioRequestMapperStruct {

    /**
     * Convierte un DTO de creación de usuario a una entidad Usuario.
     * @param dto DTO con los datos del usuario a crear
     * @return Entidad Usuario
     */
    Usuario toEntity(UsuarioCrearRequestDTO dto);


    /**
     * Convierte una lista de DTOs de creación de usuario a una lista de entidades Usuario.
     * @param dtos Lista de DTOs con los datos de usuarios a crear
     * @return Lista de entidades Usuario
     */
    List<Usuario> toEntityList( List<UsuarioCrearRequestDTO> dtos);

    /**
     * Actualiza una entidad Usuario existente a partir de un DTO de actualización.
     * Anotaciones:
     * - @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
     *   -> Ignora los campos nulos del DTO para no sobreescribir los valores existentes en la entidad.
     * - @MappingTarget
     *   -> Indica que se debe mapear sobre la entidad existente y no crear una nueva instancia.
     * @param dto DTO con los campos a actualizar
     * @param usuario Entidad Usuario que se actualizará
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateEntity(UsuarioActualizarDTO dto, @MappingTarget Usuario usuario);

}
