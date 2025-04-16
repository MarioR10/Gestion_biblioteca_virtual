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

public interface PrestamoRepository extends JpaRepository<Prestamo, UUID> {

    @Query("SELECT p FROM Prestamo p JOIN FETCH p.libro JOIN FETCH p.usuario WHERE p.id = :id")
    Optional<Prestamo> findById(@Param( "id") UUID id);
    Page<Prestamo> findPrestamoByUsuarioIdAndEstado(UUID usuarioId, String estado, Pageable pageable);
    Page<Prestamo> findByUsuarioId( UUID id, Pageable pageable);
    Page<Prestamo> findByEstado( String estado, Pageable pageable);

    @Query("SELECT p FROM Prestamo  p JOIN FETCH p.libro JOIN FETCH p.usuario")
    Page<Prestamo> findAll(Pageable pageable);
}

