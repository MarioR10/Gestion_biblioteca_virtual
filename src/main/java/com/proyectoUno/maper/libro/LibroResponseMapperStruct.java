package com.proyectoUno.maper.libro;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.entity.Libro;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper de MapStruct para convertir entre la entidad Libro y sus DTOs de respuesta.
 * Esta interfaz permite:
 * 1. Mapear un objeto Libro a su DTO de respuesta LibroResponseDTO.
 * 2. Mapear un Page de Libro a un Page de LibroResponseDTO,
 *    preservando la paginación original.
 * MapStruct genera la implementación de esta interfaz en tiempo de compilación.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LibroResponseMapperStruct {

    /**
     * Convierte una entidad Libro en un DTO de respuesta.
     * @param libro Entidad Libro
     * @return DTO de respuesta LibroResponseDTO
     */
    LibroResponseDTO toResponseDTO(Libro libro);

    /**
     * Convierte un Page de entidades Libro en un Page de DTOs de respuesta.
     * @param libros Página de entidades Libro
     * @return Página de DTOs LibroResponseDTO, manteniendo la paginación original
     */

    default  Page <LibroResponseDTO> toResponseDTOPage(Page<Libro> libros){

        List<LibroResponseDTO> dtoList = libros.getContent()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return new PageImpl <>(dtoList, libros.getPageable(), libros.getTotalElements());
    }
}
