package com.proyectoUno.controller;

import com.proyectoUno.dto.reponse.PrestamoResponseDTO;
import com.proyectoUno.dto.request.prestamo.PrestamoCrearRequestDTO;
import com.proyectoUno.service.External.interfaces.PrestamoServiceExternal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/prestamo")
public class PrestamoController {


    private final PrestamoServiceExternal prestamoServiceExternal;


    public PrestamoController(PrestamoServiceExternal prestamoServiceExternal){

        this.prestamoServiceExternal=prestamoServiceExternal;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void crearPrestamo(@Valid @RequestBody PrestamoCrearRequestDTO request){
        prestamoServiceExternal.crearPrestamo(request);
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


    //Metodos paginados



    @GetMapping("/historial/{id}")
    public ResponseEntity<Page<PrestamoResponseDTO>> encontrarHistorialPrestamosPorUsuario(
            @PathVariable UUID id,
            @PageableDefault( page = 0, size = 8) Pageable pageable){

        Page<PrestamoResponseDTO> historialDePrestamos = prestamoServiceExternal.encontrarHistorialDePrestamoPorUsuario(id, pageable);

        return ResponseEntity.ok(historialDePrestamos);

    }

    @GetMapping("/activos/{id}")
    public ResponseEntity<Page<PrestamoResponseDTO>> obtenerPrestamosActivosPorUsuario(
            @PathVariable UUID id,
            @PageableDefault(page = 0, size = 8)Pageable pageable){

        Page<PrestamoResponseDTO> prestamosPorUsuario = prestamoServiceExternal.encontrarPrestamosActivosPorUsuarios(id,pageable);
        return ResponseEntity.ok(prestamosPorUsuario);
    }


    @GetMapping("/activos")
    public ResponseEntity< Page<PrestamoResponseDTO>> encontrarPrestamosActivos(

            @PageableDefault(page = 0, size = 8) Pageable pageable
    ){

        Page<PrestamoResponseDTO> prestamosActivos = prestamoServiceExternal.encontrarPrestamosActivos(pageable);

        return  ResponseEntity.ok(prestamosActivos);
    }

    @GetMapping()
    public ResponseEntity<Page<PrestamoResponseDTO>> enconctrarPrestamos(
            @PageableDefault(page = 0, size = 8) Pageable pageable
    ){

        Page<PrestamoResponseDTO> prestamos= prestamoServiceExternal.encontrarPrestamosConLibroYUsuarioAnidados(pageable);
        return ResponseEntity.ok(prestamos);
    }
}
