package com.proyectoUno.repository;

import com.proyectoUno.entity.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LibroRepository  extends JpaRepository<Libro, UUID> {

    Page<Libro> findByTituloContaining(String titulo, Pageable pageable);
    Page<Libro> findAllByAutorContaining(String autor, Pageable pageable);
    Page<Libro> findLibroByIsbnContaining(String isbn, Pageable pageable);
    Page<Libro> findLibroByCategoriaContaining(String categoria, Pageable pageable);
    Page<Libro> findLibroByEstadoContaining(String estado, Pageable pageable);
   
}
