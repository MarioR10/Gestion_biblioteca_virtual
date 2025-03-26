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
import java.util.UUID;

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

    @GetMapping("/activos/{usuarioId}")
    public ResponseEntity<List<PrestamoResponseDTO>> obtenerPrestamosActivosPorUsuario(@PathVariable UUID usuarioId){

        List<PrestamoResponseDTO> prestamosActivos = prestamoServiceExternal.encontrarPrestamosActivosPorUsuarios(usuarioId);

        return ResponseEntity.ok(prestamosActivos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> obtenerPrestamoPorId(@PathVariable UUID id){


        PrestamoResponseDTO prestamo = prestamoServiceExternal.encontrarPrestamoPorId(id);

        return ResponseEntity.ok(prestamo);
    }

    @PutMapping("/devolucion/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void registrarDevolucion(@PathVariable UUID id){

        prestamoServiceExternal.registrarDevolucion(id);
    }

    @GetMapping("/historial/{usuarioId}")
    public ResponseEntity<List<PrestamoResponseDTO>> obtenerHistorialPrestamosPorUsuario(@PathVariable UUID usuarioId){

       List<PrestamoResponseDTO> prestamos= prestamoServiceExternal.obtenerHistorialDePrestamoPorUsuario(usuarioId);

       return ResponseEntity.ok(prestamos);
    }







}
