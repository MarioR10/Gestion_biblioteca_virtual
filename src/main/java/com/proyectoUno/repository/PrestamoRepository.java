package com.proyectoUno.repository;

import com.proyectoUno.entity.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad Prestamo.
 * Proporciona operaciones CRUD básicas a través de JpaRepository
 * y consultas personalizadas con JOIN FETCH para evitar problemas
 * de LazyInitialization y cargar usuario y libro relacionados.
 * Todos los métodos devuelven resultados paginados cuando es necesario.
 */
public interface PrestamoRepository extends JpaRepository<Prestamo, UUID> {

    /** Obtiene un préstamo por su ID, cargando también usuario y libro asociados */
    @Query("SELECT p FROM Prestamo p JOIN FETCH p.libro JOIN FETCH p.usuario WHERE p.id = :id")
    Optional<Prestamo> findById(@Param( "id") UUID id);

    /** Obtiene préstamos de un usuario por estado específico, paginados */
    @Query("SELECT p FROM Prestamo p JOIN FETCH p.libro JOIN FETCH p.usuario WHERE p.usuario.id =:usuarioId AND p.estado =:estado")
    Page<Prestamo> findPrestamoByUsuarioIdAndEstado(@Param("usuarioId") UUID usuarioId,@Param("estado") String estado, Pageable pageable);

    /** Obtiene todos los préstamos de un usuario, paginados */
    @Query("SELECT p FROM Prestamo  p JOIN FETCH  p.libro JOIN FETCH p.usuario WHERE p.usuario.id =: usuarioId")
    Page<Prestamo> findByUsuarioId(@Param("usuarioId") UUID id, Pageable pageable);

    /** Obtiene todos los préstamos por estado, paginados */
    @Query( "SELECT  p FROM  Prestamo  p JOIN FETCH p.libro JOIN FETCH  p.usuario WHERE p.estado =: estado")
    Page<Prestamo> findByEstado( String estado, Pageable pageable);

    /** Obtiene todos los préstamos, cargando usuario y libro relacionados, paginados */
    @Query("SELECT p FROM Prestamo p JOIN FETCH p.libro JOIN FETCH p.usuario")
    Page<Prestamo> findAll(Pageable pageable);
}

