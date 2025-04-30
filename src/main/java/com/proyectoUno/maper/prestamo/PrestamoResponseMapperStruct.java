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

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
uses = {UsuarioResponseMapperStruct.class, LibroResponseMapperStruct.class})
public interface PrestamoResponseMapperStruct {

    PrestamoResponseDTO toResponseDTO(Prestamo prestamo);
    default Page<PrestamoResponseDTO> toResponseDTOPage(Page<Prestamo> prestamos){

        List<PrestamoResponseDTO> dtoList = prestamos.getContent()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, prestamos.getPageable(), prestamos.getTotalElements());
    }
}
