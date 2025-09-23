package com.proyectoUno.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Excepción personalizada que indica que un JWT ha sido revocado.
 * Se utiliza para encapsular errores relacionados con tokens inválidos
 * y permite enviar mensajes claros cuando se detecta un token revocado.
 */
public class JwtRevokedException extends AuthenticationException {
    public JwtRevokedException(String message) {
        super(message);
    }
}
