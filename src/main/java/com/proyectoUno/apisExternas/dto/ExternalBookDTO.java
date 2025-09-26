package com.proyectoUno.apisExternas.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalBoockDTO(
        String title,
        List<String> authors,
        String description,
        String isbn13,
        List<String> categories,
        String publishDate
        ) {

}
