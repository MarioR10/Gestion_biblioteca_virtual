package com.proyectoUno.maper.libro;

import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import org.springframework.stereotype.Component;

@Component
public class LibroRequestMapper {

    // Metodo para actualizar una entidad a partir de un DTO request
    public Libro actualizarEntidadDesdeDTO(LibroActualizarRequestDTO libroRequestDTO, Libro libroExistente){


        if (libroRequestDTO.getTitulo() != null){

            libroExistente.setTitulo(libroRequestDTO.getTitulo());
        }

        if (libroRequestDTO.getAutor() != null){

            libroExistente.setAutor(libroRequestDTO.getAutor());
        }


        if (libroRequestDTO.getCategoria() != null){

            libroExistente.setCategoria(libroRequestDTO.getCategoria());
        }

        if (libroRequestDTO.getEstado() != null){

            libroExistente.setEstado(libroRequestDTO.getEstado());
        }

        return libroExistente;

    }

    // Metodo para convertir un DTO request  en una entidad (para creación)

    public Libro crearEntidadDesdeDTO (LibroCrearRequestDTO libroRequestDTO) {

        //Entidad
        Libro libro = new Libro();

        //DTO a entidad

        if (libroRequestDTO.getTitulo() != null) {
            libro.setTitulo(libroRequestDTO.getTitulo());
        }

        if (libroRequestDTO.getAutor() != null) {
            libro.setAutor(libroRequestDTO.getAutor());
        }

        if (libroRequestDTO.getIsbn() != null) {
            libro.setIsbn(libroRequestDTO.getIsbn());
        }

        if (libroRequestDTO.getCategoria() != null) {
            libro.setCategoria(libroRequestDTO.getCategoria());
        }

        if (libroRequestDTO.getAnioDePublicacion() != null) {
            libro.setAnioDePublicacion(libroRequestDTO.getAnioDePublicacion());
        }

        //Retonarmos la Entidad
        return libro;

    }

}
