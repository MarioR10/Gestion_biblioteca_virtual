package com.proyectoUno.maper.libro;

import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LibroRequestMapperStruct {

    Libro toEntity(LibroCrearRequestDTO dto);
    Libro  toEntity(LibroActualizarRequestDTO dto);
    List<Libro> toEntityList(List<LibroCrearRequestDTO> dtos);


}
