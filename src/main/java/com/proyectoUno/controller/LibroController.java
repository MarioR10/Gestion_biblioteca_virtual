package com.proyectoUno.controller;

import com.proyectoUno.dto.reponse.LibroResponseDTO;

import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.service.interfaces.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para la entidad Libro.
 * Proporciona endpoints para CRUD y búsquedas filtradas con paginación.
 * La seguridad de acceso a cada método se define en SecurityConfiguration.
 */
@RestController
@RequestMapping("/api/libro") //Base path para todos los endpoints
public class LibroController {

    private final LibroService libroService;


    /**
     * Constructor con inyección de dependencias del servicio de Libro.
     * @param libroService servicio para operaciones sobre libros
     */
    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    // ==========================
    // Métodos CRUD básicos
    // ==========================

    /**
     * Obtiene un libro por su ID.
     * @param id UUID del libro
     * @return DTO con los datos del libro
     */

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtenerLibroPorId(@PathVariable UUID id){
        LibroResponseDTO libro = libroService.encontrarLibroPorId(id);
        return ResponseEntity.ok(libro);
    }

    /**
     * Elimina un libro por su ID.
     * Solo accesible por administradores.
     * @param id UUID del libro a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarLibroPorId(@PathVariable UUID id){
        libroService.eliminarLibroPorId(id);
    }

    /**
     * Actualiza los campos de un libro existente.
     * Solo accesible por administradores.
     * @param id      UUID del libro a actualizar
     * @param request DTO con los campos a actualizar
     * @return DTO con los datos actualizados
     */
    @PatchMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> actualizarLibro(@PathVariable UUID id, @Valid @RequestBody LibroActualizarRequestDTO request){
        LibroResponseDTO libroActualizado = libroService.actualizarLibro(id,request);
        return ResponseEntity.ok(libroActualizado);
    }

    /**
     * Crea uno o varios libros.
     * Solo accesible por administradores.
     * @param requests lista de DTOs con los datos para crear libros
     */
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void crearLibro(@Valid @RequestBody List<LibroCrearRequestDTO> requests){
        libroService.crearLibro(requests);
    }


    // ==========================
    // Métodos paginados
    // Todos devuelven Page<LibroResponseDTO>.
    // Si no se provee Pageable, se usa page=0, size=8 por defecto.
    // ==========================

    /**
     * Obtiene todos los libros con paginación.
     * @param pageable configuración de página y tamaño (opcional)
     * @return página de DTOs de libros
     */
    @GetMapping()
    public ResponseEntity<Page<LibroResponseDTO>> obtenerLibros(
            @PageableDefault(page = 0, size = 8)Pageable pageable){

        Page<LibroResponseDTO> libros = libroService.encontrarLibros(pageable);
        return ResponseEntity.ok(libros);

    }

    /**
     * Filtra libros por título con paginación.
     * @param titulo   título a buscar
     * @param pageable configuración de página y tamaño (opcional)
     * @return página de DTOs de libros que coinciden con el título
     */
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<Page<LibroResponseDTO>> encontrarPorTitulo(
            @PathVariable String titulo,
            @PageableDefault(page = 0, size = 8) Pageable pageable){

        Page<LibroResponseDTO> libros = libroService.encontrarLibroPorTitulo(titulo, pageable);
        return ResponseEntity.ok(libros);

    }

    /**
     * Filtra libros por autor con paginación.
     * @param autor    autor a buscar
     * @param pageable configuración de página y tamaño (opcional)
     * @return página de DTOs de libros que coinciden con el autor
     */
    @GetMapping("/autor/{autor}")
    public ResponseEntity<Page<LibroResponseDTO>> encontrarPorAutor(
            @PathVariable String autor,
            @PageableDefault(page=0,size = 8) Pageable pageable){

        Page<LibroResponseDTO> libros= libroService.encontrarLibroPorAutor(autor,pageable);
        return  ResponseEntity.ok(libros);
    }

    /**
     * Filtra libros por ISBN con paginación.
     * @param isbn     ISBN a buscar
     * @param pageable configuración de página y tamaño (opcional)
     * @return página de DTOs de libros que coinciden con el ISBN
     */
    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Page<LibroResponseDTO>> encontrarPorIsbn(
            @PathVariable String isbn,
            @PageableDefault(page = 0, size = 8) Pageable pageable){

        Page<LibroResponseDTO> libros = libroService.encontrarLibroPorIsbn(isbn,pageable);
        return ResponseEntity.ok(libros);
    }

    /**
     * Filtra libros por categoría con paginación.
     * @param categoria categoría a buscar
     * @param pageable  configuración de página y tamaño (opcional)
     * @return página de DTOs de libros que coinciden con la categoría
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<Page<LibroResponseDTO>> encontrarPorCategoria(
            @PathVariable String categoria,
            @PageableDefault( page = 0, size = 8) Pageable pageable){

        Page<LibroResponseDTO> libros = libroService.encontrarLibroPorCategoria(categoria,pageable);
        return ResponseEntity.ok(libros);
    }

    /**
     * Filtra libros por estado con paginación.
     * @param estado   estado a buscar
     * @param pageable configuración de página y tamaño (opcional)
     * @return página de DTOs de libros que coinciden con el estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<LibroResponseDTO>> encontarPorCategoria(
            @PathVariable String estado,
            @PageableDefault(page = 0,size = 8) Pageable pageable){
        Page <LibroResponseDTO> libros = libroService.encontrarLibroPorEstado(estado, pageable);
        return ResponseEntity.ok(libros);
    }
}