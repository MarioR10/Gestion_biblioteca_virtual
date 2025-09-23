package com.proyectoUno.maper.libro;

import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import org.mapstruct.*;


import java.util.List;

/**
 * Mapper de MapStruct para convertir entre DTOs de Libro y la entidad Libro.
 * Esta interfaz permite:
 * 1. Convertir DTOs de creación en entidades.
 * 2. Convertir listas de DTOs en listas de entidades.
 * 3. Actualizar una entidad existente a partir de un DTO de actualización,
 *    ignorando los valores null para no sobrescribir datos existentes.
 * MapStruct genera la implementación de esta interfaz en tiempo de compilación.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LibroRequestMapperStruct {

    /**
     * Convierte un DTO de creación en una nueva entidad Libro.
     * @param dto DTO con los datos del libro a crear
     * @return Entidad Libro
     */
    Libro toEntity(LibroCrearRequestDTO dto);

    /**
     * Convierte una lista de DTOs de creación en una lista de entidades Libro.
     * @param dtos Lista de DTOs
     * @return Lista de entidades Libro
     */
    List<Libro> toEntityList(List<LibroCrearRequestDTO> dtos);

    /**
     * Actualiza una entidad existente con los valores del DTO de actualización.
     * @param dto DTO con los nuevos valores
     * @param libro Entidad existente a actualizar
     * @BeanMapping: Configura cómo MapStruct maneja los valores null:
     *               NullValuePropertyMappingStrategy.IGNORE => ignora campos null para no sobreescribir.
     * @MappingTarget: Indica que no se crea una nueva instancia, sino que se actualiza la entidad pasada como parámetro.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void  toUpdateEntity(LibroActualizarRequestDTO dto, @MappingTarget Libro libro);


}
