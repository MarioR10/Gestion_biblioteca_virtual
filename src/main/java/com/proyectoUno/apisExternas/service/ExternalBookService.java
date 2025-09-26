package com.proyectoUno.apisExternas.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyectoUno.apisExternas.dto.GoogleBoockDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class GoogleBooksService {

    private final RestTemplate restTemplate; //sirve para hacer peticiones a APIs externas
    private final ObjectMapper objectMapper;
    @Value("${google.books.api.key}")
    private String apiKey;

    @Value("${google.books.api.key}")
    private String apiUrl;


    public GoogleBooksService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Page<GoogleBoockDTO> buscarLibrosPorAtributo(String atributo){

        String url = apiUrl +"q="+atributo+"&key="+apiKey;
        GoogleBoockDTO response= restTemplate.getForObject(url, GoogleBoockDTO.class);

        JsonNode response = restTemplate.getForObject(url,JsonNode.class);
        JsonNode itemsNode = response != null?

    }
}
