package com.proyectoUno.controller;

import com.proyectoUno.dto.reponse.PrestamoResponseDTO;
import com.proyectoUno.dto.request.prestamo.PrestamoCrearRequestDTO;
import com.proyectoUno.entity.Prestamo;
import com.proyectoUno.service.External.interfaces.PrestamoServiceExternal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/prestamo")
public class PrestamoController {


    private final PrestamoServiceExternal prestamoServiceExternal;


    public PrestamoController(PrestamoServiceExternal prestamoServiceExternal){

        this.prestamoServiceExternal=prestamoServiceExternal;
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearPrestamo(@Valid @RequestBody PrestamoCrearRequestDTO request){

        prestamoServiceExternal.guardarPrestamo(request);

    }







}
