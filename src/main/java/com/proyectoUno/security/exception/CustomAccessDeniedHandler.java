package com.proyectoUno.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@Component
public class CustomAccessDeniedHandler  implements AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper= new ObjectMapper();


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String path = request.getRequestURI();
        logger.error("Error de Autorizacion en: {}, {}",path,accessDeniedException.getMessage(),accessDeniedException);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN,
                accessDeniedException.getMessage(),
                path,
                "ACCESS_DENIED"

        );

        //Creamos la respuesta que le mandaremos al cliente
        //1. Definimos la cabecera HTTP de la respuesta ( Content-Type)
        response.setContentType("aplication/json");
        //2. Definimos el estado http de la respuesta
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        try ( var writer= response.getWriter()){

            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            logger.info("Respondiendo con JSON para {}: {}", path, jsonResponse); // Depuración
            writer.write(jsonResponse);
            writer.flush();

        }catch (IOException e) {
            logger.error("Fallo al serializar o escribir la respuesta JSON: {}", e.getMessage(), e);

            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la respuesta");
        }
    }



}
