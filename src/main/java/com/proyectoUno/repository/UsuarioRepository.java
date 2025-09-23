package com.proyectoUno.repository;

import com.proyectoUno.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad Usuario.
 * Proporciona operaciones CRUD básicas a través de JpaRepository
 * y consultas adicionales para búsquedas específicas de usuario.
 * Incluye métodos paginados para manejar grandes cantidades de datos.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    /**
     * Busca un usuario por su email.
     * Devuelve un Optional que puede estar vacío si no se encuentra el usuario.
     */
    Optional <Usuario> findByEmail(String email);
    //Busca y devuelve todos los usuarios cuyo email se encuentre en la lista proporcionada

    /**
     * Busca y devuelve todos los usuarios cuyo email se encuentre en la lista proporcionada.
     * Útil para validar múltiples emails a la vez.
     */
    List <Usuario> findByEmailIn(List<String> emails);

    /**
     * Obtiene todos los usuarios de manera paginada.
     * Permite manejar grandes cantidades de datos de manera eficiente.
     */
    Page<Usuario> findAll(Pageable pageable);
}
