package com.proyectoUno.controller;

import com.proyectoUno.dto.reponse.LibroResponseDTO;

import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.service.External.interfaces.LibroServiceExternal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/libro") //Base path para todos los endpoints
public class LibroController {

    private final LibroServiceExternal libroServiceExternal;


    //Endpoints actuales

    @Autowired
    public LibroController(LibroServiceExternal libroServiceExternal) {
        this.libroServiceExternal = libroServiceExternal;
    }


    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> obtenerLibroPorId(@PathVariable UUID id){
        LibroResponseDTO libro = libroServiceExternal.encontrarLibroPorId(id);
        return ResponseEntity.ok(libro);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarLibroPorId(@PathVariable UUID id){
        libroServiceExternal.eliminarLibroPorId(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> actualizarLibro(@PathVariable UUID id, @Valid @RequestBody LibroActualizarRequestDTO request){
        LibroResponseDTO libroActualizado = libroServiceExternal.actualizarLibro(id,request);
        return ResponseEntity.ok(libroActualizado);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void crearLibro(@Valid @RequestBody List<LibroCrearRequestDTO> requests){
        libroServiceExternal.crearLibro(requests);
    }


    //metodos paginados

    @GetMapping()
    public ResponseEntity<Page<LibroResponseDTO>> obtenerLibros(
            @PageableDefault(page = 0, size = 8)Pageable pageable){

        Page<LibroResponseDTO> libros =libroServiceExternal.encontrarLibros(pageable);
        return ResponseEntity.ok(libros);

    }
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<Page<LibroResponseDTO>> encontrarPorTitulo(
            @PathVariable String titulo,
            @PageableDefault(page = 0, size = 8) Pageable pageable){

        Page<LibroResponseDTO> libros = libroServiceExternal.encontrarLibroPorTitulo(titulo, pageable);
        return ResponseEntity.ok(libros);

    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<Page<LibroResponseDTO>> encontrarPorAutor(
            @PathVariable String autor,
            @PageableDefault(page=0,size = 8) Pageable pageable){

        Page<LibroResponseDTO> libros= libroServiceExternal.encontrarLibroPorAutor(autor,pageable);
        return  ResponseEntity.ok(libros);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<Page<LibroResponseDTO>> encontrarPorIsbn(
            @PathVariable String isbn,
            @PageableDefault(page = 0, size = 8) Pageable pageable){

        Page<LibroResponseDTO> libros = libroServiceExternal.encontrarLibroPorIsbn(isbn,pageable);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<Page<LibroResponseDTO>> encontrarPorCategoria(
            @PathVariable String categoria,
            @PageableDefault( page = 0, size = 8) Pageable pageable){

        Page<LibroResponseDTO> libros = libroServiceExternal.encontrarLibroPorCategoria(categoria,pageable);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<Page<LibroResponseDTO>> encontarPorCategoria(
            @PathVariable String estado,
            @PageableDefault(page = 0,size = 8) Pageable pageable){
        Page <LibroResponseDTO> libros = libroServiceExternal.encontrarLibroPorEstado(estado, pageable);
        return ResponseEntity.ok(libros);
    }

}





