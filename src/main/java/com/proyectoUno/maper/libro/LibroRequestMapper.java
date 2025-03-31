package com.proyectoUno.maper.libro;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LibroRequestMapper {

    // Metodo para actualizar una entidad a partir de un DTO request
    public Libro actualizarEntidadDesdeDTO(LibroActualizarRequestDTO libroRequestDTO){

            Libro libro = new Libro();
            libro.setTitulo(libroRequestDTO.getTitulo());
            libro.setAutor(libroRequestDTO.getAutor());
            libro.setCategoria(libroRequestDTO.getCategoria());
            libro.setAnioDePublicacion(libroRequestDTO.getAnioDePublicacion());
        return libro;

    }

    // Metodo para convertir un DTO request  en una entidad (para creación)

    public Libro crearEntidadDesdeDTO (LibroCrearRequestDTO libroRequestDTO) {

        //Entidad
        Libro libro = new Libro();

        //DTO a entidad

            libro.setTitulo(libroRequestDTO.getTitulo());

            libro.setAutor(libroRequestDTO.getAutor());

            libro.setIsbn(libroRequestDTO.getIsbn());

            libro.setCategoria(libroRequestDTO.getCategoria());

            libro.setAnioDePublicacion(libroRequestDTO.getAnioDePublicacion());


        //Retonarmos la Entidad
        return libro;

    }

    //Metodo para convertir a lista de Entidades
    // Convertir una lista de entidades en una lista de DTOs de respuesta
    public List<Libro> convertirAListaEntidad(List<LibroCrearRequestDTO> libros){


        return libros.stream()
                .map(this::crearEntidadDesdeDTO)// Convierte cada Libro a un DTO (nuestro metodo anterior)
                .collect(Collectors.toList()); //  Recolecta los resultados en una lista ahora de tipo DTO
    }


}
