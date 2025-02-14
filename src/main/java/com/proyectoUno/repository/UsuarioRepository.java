package com.proyectoUno.repository;

import com.proyectoUno.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional <Usuario> findByEmail(String email);

    //Ya vienen los CRUD basicos, si necesitamos metodos adicionales se agregan aqui;
}
