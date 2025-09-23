package com.proyectoUno.repository;

import com.proyectoUno.entity.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad Libro.
 * Proporciona operaciones CRUD básicas a través de JpaRepository
 * y métodos adicionales para búsquedas paginadas por diferentes campos.
 * Todos los métodos que contienen 'Containing' realizan búsquedas
 * parciales (similar a LIKE en SQL) y devuelven resultados paginados.
 */
public interface LibroRepository  extends JpaRepository<Libro, UUID> {

    /** Busca libros cuyo título contenga la cadena especificada */
    Page<Libro> findByTituloContaining(String titulo, Pageable pageable);

    /** Busca libros cuyo autor contenga la cadena especificada */
    Page<Libro> findAllByAutorContaining(String autor, Pageable pageable);

    /** Busca libros cuyo ISBN contenga la cadena especificada */
    Page<Libro> findLibroByIsbnContaining(String isbn, Pageable pageable);

    /** Busca libros cuya categoría contenga la cadena especificada */
    Page<Libro> findLibroByCategoriaContaining(String categoria, Pageable pageable);

    /** Busca libros cuyo estado contenga la cadena especificada */
    Page<Libro> findLibroByEstadoContaining(String estado, Pageable pageable);
   
}
