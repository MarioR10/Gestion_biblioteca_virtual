package com.proyectoUno.maper.libro;

import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import org.springframework.stereotype.Component;

@Component
public class LibroRequestMapper {

    // Metodo para actualizar una entidad a partir de un DTO request
    public Libro actualizarEntidadDesdeDTO(LibroActualizarRequestDTO libroRequestDTO){

            Libro libro = new Libro();
            libro.setTitulo(libroRequestDTO.getTitulo());
            libro.setAutor(libroRequestDTO.getAutor());
            libro.setCategoria(libroRequestDTO.getCategoria());
            libro.setEstado(libroRequestDTO.getEstado());

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

            libro.setEstado(libroRequestDTO.getEstado());

            libro.setAnioDePublicacion(libroRequestDTO.getAnioDePublicacion());

        //Retonarmos la Entidad
        return libro;

    }

}
