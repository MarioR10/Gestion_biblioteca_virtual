package com.proyectoUno.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class demoController {

    @GetMapping("/hola")

    public String TestRestAPI(){

        String message = "Hola, esta es una prueba";

        return message;
    }
}
