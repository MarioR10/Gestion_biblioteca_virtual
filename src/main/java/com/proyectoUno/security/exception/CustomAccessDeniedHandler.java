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

@Component
public class CustomAccessDeniedHandler  implements AccessDeniedHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
    private final ObjectMapper objectMapper= new ObjectMapper().registerModule(new JavaTimeModule());

     /*
    El metodo recibe tres parametros importantes para su funcionamiento:
        1. HttpServletRequest que representa la solicitud http que el cliente envio al servidor
        2. HttpServletResponse representa la respuesta que nuestra aplicacion le mostrara al cliente.
        3. es la excepcion que contiene los detalles del error de autorización que ocurrio.
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
            logger.error("Fallo al serializar o escribir la respuesta JSON: {}", e.getMessage(), e);

            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la respuesta");
        }
    }



}
