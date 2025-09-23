package com.proyectoUno.maper.prestamo;


import com.proyectoUno.dto.reponse.PrestamoResponseDTO;
import com.proyectoUno.entity.Prestamo;
import com.proyectoUno.maper.libro.LibroResponseMapperStruct;
import com.proyectoUno.maper.usuario.UsuarioResponseMapperStruct;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper de MapStruct para convertir entre la entidad Prestamo y sus DTOs de respuesta.
 * Esta interfaz permite:
 * 1. Mapear un objeto Prestamo a su DTO de respuesta PrestamoResponseDTO.
 * 2. Mapear un Page de Prestamo a un Page de PrestamoResponseDTO,
 *    preservando la paginación original.
 * Se utilizan los mappers de Usuario y Libro para mapear los campos relacionados
 * dentro de la entidad Prestamo.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
uses = {UsuarioResponseMapperStruct.class, LibroResponseMapperStruct.class})
public interface PrestamoResponseMapperStruct {

    /**
     * Convierte una entidad Prestamo en un DTO de respuesta.
     * @param prestamo Entidad Prestamo
     * @return DTO de respuesta PrestamoResponseDTO
     */
    PrestamoResponseDTO toResponseDTO(Prestamo prestamo);

    /**
     * Convierte un Page de entidades Prestamo en un Page de DTOs de respuesta.
     * @param prestamos Página de entidades Prestamo
     * @return Página de DTOs PrestamoResponseDTO, manteniendo la paginación original
     */
    default Page<PrestamoResponseDTO> toResponseDTOPage(Page<Prestamo> prestamos){

        List<PrestamoResponseDTO> dtoList = prestamos.getContent()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, prestamos.getPageable(), prestamos.getTotalElements());
    }
}
