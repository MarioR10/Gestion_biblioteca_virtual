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

@RestController
@RequestMapping("/api/libro") //Base path para todos los endpoints
public class LibroController {

    private final LibroService libroService;


    //Endpoints actuales

    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtenerLibroPorId(@PathVariable UUID id){
        LibroResponseDTO libro = libroService.encontrarLibroPorId(id);
        return ResponseEntity.ok(libro);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarLibroPorId(@PathVariable UUID id){
        libroService.eliminarLibroPorId(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> actualizarLibro(@PathVariable UUID id, @Valid @RequestBody LibroActualizarRequestDTO request){
        LibroResponseDTO libroActualizado = libroService.actualizarLibro(id,request);
        return ResponseEntity.ok(libroActualizado);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void crearLibro(@Valid @RequestBody List<LibroCrearRequestDTO> requests){
        libroService.crearLibro(requests);
    }


    //metodos paginados

    @GetMapping()
    public ResponseEntity<Page<LibroResponseDTO>> obtenerLibros(
            @PageableDefault(page = 0, size = 8)Pageable pageable){

        Page<LibroResponseDTO> libros = libroService.encontrarLibros(pageable);
        return ResponseEntity.ok(libros);

    }
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<Page<LibroResponseDTO>> encontrarPorTitulo(
            @PathVariable String titulo,
            @PageableDefault(page = 0, size = 8) Pageable pageable){

        Page<LibroResponseDTO> libros = libroService.encontrarLibroPorTitulo(titulo, pageable);
        return ResponseEntity.ok(libros);

    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<Page<LibroResponseDTO>> encontrarPorAutor(
            @PathVariable String autor,
            @PageableDefault(page=0,size = 8) Pageable pageable){

        Page<LibroResponseDTO> libros= libroService.encontrarLibroPorAutor(autor,pageable);
        return  ResponseEntity.ok(libros);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Page<LibroResponseDTO>> encontrarPorIsbn(
            @PathVariable String isbn,
            @PageableDefault(page = 0, size = 8) Pageable pageable){

        Page<LibroResponseDTO> libros = libroService.encontrarLibroPorIsbn(isbn,pageable);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<Page<LibroResponseDTO>> encontrarPorCategoria(
            @PathVariable String categoria,
            @PageableDefault( page = 0, size = 8) Pageable pageable){

        Page<LibroResponseDTO> libros = libroService.encontrarLibroPorCategoria(categoria,pageable);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<LibroResponseDTO>> encontarPorCategoria(
            @PathVariable String estado,
            @PageableDefault(page = 0,size = 8) Pageable pageable){
        Page <LibroResponseDTO> libros = libroService.encontrarLibroPorEstado(estado, pageable);
        return ResponseEntity.ok(libros);
    }

}





