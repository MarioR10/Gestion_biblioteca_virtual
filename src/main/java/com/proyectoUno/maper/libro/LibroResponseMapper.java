package com.proyectoUno.maper.libro;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.entity.Libro;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LibroResponseMapper {



    //DTO RESPONSE

    // Convertir una entidad en un DTO de respuesta
    public LibroResponseDTO convertirAResponseDTO(Libro libro){

        //DTO
        LibroResponseDTO libroResponseDTO= new LibroResponseDTO();


        //Entidad a DTO
        libroResponseDTO.setId(libro.getId());
        libroResponseDTO.setTitulo(libro.getTitulo());
        libroResponseDTO.setAutor(libro.getAutor());
        libroResponseDTO.setIsbn(libro.getIsbn());
        libroResponseDTO.setCategoria(libro.getCategoria());
        libroResponseDTO.setAnioDePublicacion(libro.getAnioDePublicacion());
        libroResponseDTO.setEstado(libro.getEstado());

        //Retorna el DTO
        return  libroResponseDTO;
    }

    // Convertir una lista de entidades en una lista de DTOs de respuesta
    public List<LibroResponseDTO> convertirAListaResponseDTO(List<Libro> libros){


       return libros.stream()
                .map(this::convertirAResponseDTO)// Convierte cada Libro a un DTO (nuestro metodo anterior)
                .collect(Collectors.toList()); //  Recolecta los resultados en una lista ahora de tipo DTO
    }

}
