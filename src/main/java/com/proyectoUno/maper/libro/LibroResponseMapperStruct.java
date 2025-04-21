package com.proyectoUno.maper.libro;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.entity.Libro;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LibroResponseMapperStruct {

    LibroResponseDTO toResponseDTO(Libro libro);
    default  Page <LibroResponseDTO> toResponseDTOPage(Page<Libro> libros){

        List<LibroResponseDTO> dtoList = libros.getContent()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl <>(dtoList, libros.getPageable(), libros.getTotalElements());
    }
}
