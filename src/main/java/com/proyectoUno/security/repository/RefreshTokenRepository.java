package com.proyectoUno.security.repository;

import com.proyectoUno.security.entity.RefreshToken;
import com.proyectoUno.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para la entidad RefreshToken.
 * Proporciona métodos para acceder y manipular tokens de actualización en la base de datos.
 */
public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, UUID> {

    /**
     * Busca un RefreshToken por su valor de token.
     * @param token El string del refresh token.
     * @return Optional que contiene el RefreshToken si existe.
     */
    Optional<RefreshToken> findRefreshTokenByToken(String token);

    /**
     * Elimina todos los refresh tokens asociados a un usuario.
     * Útil al revocar todos los tokens de un usuario al eliminar su cuenta o al cerrar sesión globalmente.
     * @param usuario El usuario cuyos tokens se eliminarán.
     */
    void deleteRefreshTokenByUsuario(Usuario usuario);


}
