package com.proyectoUno.maper.libro;

import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import org.mapstruct.*;


import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LibroRequestMapperStruct {

    Libro toEntity(LibroCrearRequestDTO dto);
    List<Libro> toEntityList(List<LibroCrearRequestDTO> dtos);

    //Metodo para mapear DTO -> Entidad existente
    //La primera anotacion le indica a MapStruct que si un campo del DTO es null, no la incluya al mapeo de la entidad
    //La segunda anotacion le indica a MapStruct que no cree una nueva instancia (Entidad), si no que se enfoque (Target) en la que le pasamos
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void  toUpdateEntity(LibroActualizarRequestDTO dto, @MappingTarget Libro libro);


}
