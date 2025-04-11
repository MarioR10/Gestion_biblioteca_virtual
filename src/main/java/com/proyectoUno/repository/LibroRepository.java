package com.proyectoUno.repository;

import com.proyectoUno.entity.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LibroRepository  extends JpaRepository<Libro, UUID> {

    //metodosactuales sin paginacion

    // Encontrar por título (búsqueda parcial)
    List<Libro> findLibroByTituloContaining(String titulo);
    // Encontrar por autor (búsqueda parcial)
    List<Libro> findAllByAutorContaining(String autor);
    // Encontrar por ISBN
    List<Libro> findLibroByIsbn(String isbn);
    // Encontrar por categoria (búsqueda parcial)
    List <Libro> findLibroByCategoriaContaining(String categoria);
    // Encontrar por estado
    List<Libro> findByEstado(String estado);

    //metodos nuevos paginados

    Page<Libro> findByTituloContaining(String titulo, Pageable pageable);
    Page<Libro> findAllByAutorContaining(String autor, Pageable pageable);
    Page<Libro> findLibroByIsbnContaining(String isbn, Pageable pageable);
    Page<Libro> findLibroByCategoriaContaining(String categoria, Pageable pageable);
    Page<Libro> findLibroByEstadoContaining(String estado, Pageable pageable);
   
}
