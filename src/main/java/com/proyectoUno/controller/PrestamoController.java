package com.proyectoUno.controller;

import com.proyectoUno.dto.reponse.PrestamoResponseDTO;
import com.proyectoUno.dto.request.prestamo.PrestamoCrearRequestDTO;
import com.proyectoUno.service.interfaces.PrestamoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


/**
 * Controlador REST para la entidad Prestamo.
 * Proporciona endpoints para CRUD y búsquedas filtradas con paginación.
 * La seguridad de acceso a cada método se define en SecurityConfiguration.
 */
@RestController
@RequestMapping("/api/prestamo")
public class PrestamoController {


    private final PrestamoService prestamoService;

    /**
     * Constructor con inyección de dependencias del servicio de Prestamo.
     * @param prestamoService servicio para operaciones sobre prestamos
     */
    public PrestamoController(PrestamoService prestamoService){

        this.prestamoService=prestamoService;
    }

    // ==========================
    // Métodos CRUD básicos
    // ==========================

    /**
     * Crea un nuevo préstamo.
     * @param request DTO con los datos del préstamo a crear
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void crearPrestamo(@Valid @RequestBody PrestamoCrearRequestDTO request){
        prestamoService.crearPrestamo(request);
    }

    /**
     * Obtiene un préstamo por su ID.
     * @param id UUID del préstamo
     * @return DTO con los datos del préstamo
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrestamoResponseDTO> obtenerPrestamoPorId(@PathVariable UUID id){
        PrestamoResponseDTO prestamo = prestamoService.encontrarPrestamoPorId(id);
        return ResponseEntity.ok(prestamo);
    }

    /**
     * Registra la devolución de un préstamo.
     * @param id UUID del préstamo a devolver
     */
    @PutMapping("/devolucion/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void registrarDevolucion(@PathVariable UUID id){
        prestamoService.registrarDevolucion(id);
    }


    // ==========================
    // Métodos paginados
    // Todos devuelven Page<PrestamoResponseDTO>.
    // Si no se provee Pageable, se usa page=0, size=8 por defecto.
    // ==========================

    /**
     * Obtiene el historial de préstamos de un usuario específico.
     * @param id       UUID del usuario
     * @param pageable configuración de página y tamaño (opcional)
     * @return página de DTOs de préstamos del usuario
     */
    @GetMapping("/historial/{id}")
    public ResponseEntity<Page<PrestamoResponseDTO>> encontrarHistorialPrestamosPorUsuario(
            @PathVariable UUID id,
            @PageableDefault( page = 0, size = 8) Pageable pageable){

        Page<PrestamoResponseDTO> historialDePrestamos = prestamoService.encontrarHistorialDePrestamoPorUsuario(id, pageable);

        return ResponseEntity.ok(historialDePrestamos);

    }

    /**
     * Obtiene los préstamos activos de un usuario específico.
     * @param id       UUID del usuario
     * @param pageable configuración de página y tamaño (opcional)
     * @return página de DTOs de préstamos activos del usuario
     */
    @GetMapping("/activos/{id}")
    public ResponseEntity<Page<PrestamoResponseDTO>> obtenerPrestamosActivosPorUsuario(
            @PathVariable UUID id,
            @PageableDefault(page = 0, size = 8)Pageable pageable){

        Page<PrestamoResponseDTO> prestamosPorUsuario = prestamoService.encontrarPrestamosActivosPorUsuarios(id,pageable);
        return ResponseEntity.ok(prestamosPorUsuario);
    }

    /**
     * Obtiene todos los préstamos activos.
     * @param pageable configuración de página y tamaño (opcional)
     * @return página de DTOs de préstamos activos
     */
    @GetMapping("/activos")
    public ResponseEntity< Page<PrestamoResponseDTO>> encontrarPrestamosActivos(

            @PageableDefault(page = 0, size = 8) Pageable pageable
    ){

        Page<PrestamoResponseDTO> prestamosActivos = prestamoService.encontrarPrestamosActivos(pageable);

        return  ResponseEntity.ok(prestamosActivos);
    }

    /**
     * Obtiene todos los préstamos con paginación.
     * @param pageable configuración de página y tamaño (opcional)
     * @return página de DTOs de préstamos
     */
    @GetMapping()
    public ResponseEntity<Page<PrestamoResponseDTO>> encontrarPrestamos(
            @PageableDefault(page = 0, size = 8) Pageable pageable
    ){

        Page<PrestamoResponseDTO> prestamos= prestamoService.encontrarPrestamos(pageable);
        return ResponseEntity.ok(prestamos);
    }
}
