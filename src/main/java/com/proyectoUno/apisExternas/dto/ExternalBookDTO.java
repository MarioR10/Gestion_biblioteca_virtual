package com.proyectoUno.apisExternas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Record para capturar los datos crudos de Google Books API.
 * Usa List<String> para authors y categories porque la API devuelve arrays JSON.
 * Campos como title y description son String, con null si no están presentes.
 * publishedDate se mantiene como String para evitar parsing en este punto.
 * @param title
 * @param authors
 * @param description
 * @param isbn
 * @param categories
 * @param publishedDate
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalBookDTO(
        String title,
        List<String> authors,
        String description,
        String isbn,
        List<String> categories,
        String publishedDate
        ) {

}
