package com.proyectoUno.repository;

import com.proyectoUno.entity.RefreshToken;
import com.proyectoUno.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, UUID> {

//Metodo para encontrar por token

    Optional<RefreshToken> findRefreshTokenByToken(String token);
//Metodo para eliminar token por usuario

    void deleteRefreshTokenByUsuario(Usuario usuario);


}
