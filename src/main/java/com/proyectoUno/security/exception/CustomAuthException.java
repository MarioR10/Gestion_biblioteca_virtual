package com.proyectoUno.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Excepción personalizada para errores de autenticación.
 * Permite enviar mensajes específicos cuando falla la autenticación
 * y nos ayuda a envolver excepciones generadas en el filtro JWT.
 */
public class CustomAuthException extends AuthenticationException {
    public CustomAuthException(String message) {
        super(message);
    }
}
