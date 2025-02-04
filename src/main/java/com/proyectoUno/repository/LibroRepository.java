package com.proyectoUno.repository;

import com.proyectoUno.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LibroRepository  extends JpaRepository<Libro, UUID> {

    //Ya vienen los CRUD basicos, si necesitamos metodos adicionales se agregan aqui
}
