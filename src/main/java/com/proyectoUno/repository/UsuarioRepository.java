package com.proyectoUno.repository;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    //Metodos actuales
    Optional <Usuario> findByEmail(String email);
    List <Usuario> findByEmailIn(List<String> emails);

    //metodos paginados
    Page<Usuario> findAll(Pageable pageable);
}
