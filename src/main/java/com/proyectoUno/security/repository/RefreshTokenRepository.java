package com.proyectoUno.security.repository;

import com.proyectoUno.security.entity.RefreshToken;
import com.proyectoUno.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, UUID> {

//Metodo para encontrar por token
    Optional<RefreshToken> findRefreshTokenByToken(String token);

//Metodo para eliminar todos los token de un Usuario (al eliminar cuenta)
    void deleteRefreshTokenByUsuario(Usuario usuario);


}
