package com.proyectoUno.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proyectoUno.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper= new ObjectMapper().registerModule(new JavaTimeModule());

/*
El metodo recibe tres parametros importantes para su funcionamiento:
    1. HttpServletRequest que representa la solicitud http que el cliente envio al servidor
    2. HttpServletResponse representa la respuesta que nuestra aplicacion le mostrara al cliente.
    3. es la excepcion que contiene los detalles del error de autenticacion que ocurrio.
 */
@Override
public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    String path = request.getRequestURI();
    logger.info("Entrando en CustomAuthenticationEntryPoint para path: {}, Excepción: {}", path, authException.getClass().getSimpleName()); // Depuración
    logger.error("Error de Autenticación en {}: {}", path, authException.getMessage(), authException);

    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNAUTHORIZED,
            authException.getMessage(),
            path,
            "AUTH_ERROR"
    );

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    try (var writer = response.getWriter()) {
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        logger.info("Respondiendo con JSON para {}: {}", path, jsonResponse); // Depuración
        writer.write(jsonResponse);
        writer.flush(); // Asegura que se envíe la respuesta
    } catch (IOException e) {
        logger.error("Fallo al serializar o escribir la respuesta JSON: {}", e.getMessage(), e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la respuesta");
    }
}
}
