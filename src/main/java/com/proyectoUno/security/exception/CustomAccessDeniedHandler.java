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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Manejador personalizado de accesos denegados en Spring Security.
 * Esta clase se ejecuta cuando un usuario autenticado intenta acceder a un recurso para el cual no tiene permisos
 * suficientes (rol, autoridad, etc.).
 * Funcionalidad principal:
 * 1. Captura la excepción AccessDeniedException.
 * 2. Genera un ErrorResponse estandarizado con código HTTP 403 (FORBIDDEN).
 * 3. Serializa la respuesta a JSON y la envía al cliente.
 * 4. Registra en logs tanto la excepción como la respuesta enviada.
 */
@Component
public class CustomAccessDeniedHandler  implements AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    // Mapper de Jackson configurado para soportar fechas (JavaTimeModule)
    private final ObjectMapper objectMapper= new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * Maneja la excepción de acceso denegado.
     * @param request  Solicitud HTTP entrante del cliente
     * @param response Respuesta HTTP que se enviará al cliente
     * @param accessDeniedException Excepción lanzada por Spring Security cuando
     *                              el usuario no tiene permisos suficientes
     * @throws IOException si ocurre un error al escribir la respuesta
     * @throws ServletException no usado, pero requerido por la firma
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String path = request.getRequestURI();
        String errorMesage = accessDeniedException.getMessage();
        logger.error("Entradando en CustomAccessDeniedHandler para path: {}, Excepcion: {}: ",path,accessDeniedException, errorMesage);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN,
                errorMesage,
                path,
                "ACCESS_DENIED"
        );

        //Creamos la respuesta que le mandaremos al cliente
        //1. Definimos la cabecera HTTP de la respuesta ( Content-Type)
        response.setContentType("application/json");
        //2. Definimos el estado http de la respuesta
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        try ( var writer= response.getWriter()){

            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            logger.info("Respondiendo con JSON para {}: {}", path, jsonResponse); // Depuración
            writer.write(jsonResponse);
            writer.flush();// Asegura que se envíe la respuesta

        }catch (IOException e) {
            // Manejo de error al serializar o escribir la respuesta
            logger.error("Fallo al serializar o escribir la respuesta JSON: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la respuesta");
        }
    }



}
