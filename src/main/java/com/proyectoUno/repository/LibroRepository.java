package com.proyectoUno.repository;

import com.proyectoUno.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LibroRepository  extends JpaRepository<Libro, UUID> {

    //Ya vienen los CRUD basicos, si necesitamos metodos adicionales se agregan aqui

    // Containing indica que se va a hacer una búsqueda que podría devolver más de un libro cuyo título contenga la cadena que estamos  buscando

    // Encontrar por título (búsqueda parcial)
    List<Libro> findLibroByTituloContaining(String titulo);

    // Encontrar por autor (búsqueda parcial)
    List<Libro> findAllByAutorContaining(String autor);

    // Encontrar por ISBN
    Optional<Libro> findLibroByIsbn(String isbn);

    //Encontrar por categoria

    // Encontrar por categoria (búsqueda parcial)
    List <Libro> findLibroByCategoriaContaining(String categoria);

    // Encontrar por estado
    List<Libro> findByEstado(String estado);

   
}
