package com.proyectoUno.maper.prestamo;

import com.proyectoUno.dto.reponse.PrestamoResponseDTO;
import com.proyectoUno.entity.Prestamo;
import com.proyectoUno.maper.libro.LibroResponseMapper;
import com.proyectoUno.maper.usuario.UsuarioResponseMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrestamoResponseMapper {
    private final LibroResponseMapper libroResponseMapper;
    private final UsuarioResponseMapper usuarioResponseMapper;

    public PrestamoResponseMapper(LibroResponseMapper libroResponseMapper, UsuarioResponseMapper usuarioResponseMapper) {
        this.libroResponseMapper = libroResponseMapper;
        this.usuarioResponseMapper = usuarioResponseMapper;
    }


    //Metodo para convertir una entidad a un DTO Response

    public PrestamoResponseDTO convertirAresponseDTO(Prestamo prestamo){

        PrestamoResponseDTO prestamoResponseDTO = new PrestamoResponseDTO();

        prestamoResponseDTO.setId(prestamo.getId());
        prestamoResponseDTO.setFechaPrestamo(prestamo.getFechaPrestamo());
        prestamoResponseDTO.setFechaDevolucion(prestamo.getFechaDevolucion());
        prestamoResponseDTO.setEstado(prestamo.getEstado());
        //anidados
        prestamoResponseDTO.setLibroAsociado(libroResponseMapper.convertirAResponseDTO(prestamo.getLibro()));
        prestamoResponseDTO.setUsuarioAsociado(usuarioResponseMapper.convertirAResponseDTO(prestamo.getUsuario()));
        return prestamoResponseDTO;

    }


    // Convertir una lista de entidades en una lista de DTOs de respuesta
    public List<PrestamoResponseDTO> convertirAListaResponseDTO(List<Prestamo> prestamos){


        return prestamos.stream()
                .map(this::convertirAresponseDTO)// Convierte cada Libro a un DTO (nuestro metodo anterior)
                .collect(Collectors.toList()); //  Recolecta los resultados en una lista ahora de tipo DTO
    }


    //Convertir a una pagina de prestamoResponseDTO
    public Page<PrestamoResponseDTO> convertirAPageResponseDTO( Page<Prestamo> prestamos){

       return prestamos.map(this::convertirAresponseDTO);
    }
}
