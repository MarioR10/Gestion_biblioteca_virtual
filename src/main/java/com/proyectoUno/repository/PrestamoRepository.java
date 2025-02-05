package com.proyectoUno.repository;

import com.proyectoUno.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrestamoRepository extends JpaRepository<Prestamo, UUID> {

    //CONSULTAS SIGUIENDO CONVENCION DE SPRING DATA JPA (EN NOTION HAY MAS SOBRE ESTO)

    //Consultar prestamos activos por usuario y estado
    List<Prestamo> findByUsuarioIdAndEstado(UUID theid,String estado );

    //Historico de Prestamos
    List<Prestamo> findByUsuarioId(UUID theid);
}


