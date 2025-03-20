package com.proyectoUno.controller;

import com.proyectoUno.dto.reponse.LibroResponseDTO;

import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.service.External.interfaces.LibroServiceExternal;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    public LibroController(LibroServiceExternal libroServiceExternal) {

        this.libroServiceExternal = libroServiceExternal;

    }


    @GetMapping("/libros")
    public ResponseEntity<List<LibroResponseDTO>> obtenerLibros(){

        List<LibroResponseDTO> libros = libroServiceExternal.encontrarLibros();

        return ResponseEntity.ok(libros);
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

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<LibroResponseDTO> actualizarLibro(@PathVariable UUID id, @Valid @RequestBody LibroActualizarRequestDTO request){

        LibroResponseDTO libroActualizado = libroServiceExternal.actualizarLibro(id,request);

        return ResponseEntity.ok(libroActualizado);
    }

    @PostMapping("/crear")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearLibro(@RequestBody LibroCrearRequestDTO request){

        libroServiceExternal.guardarLibro(request);
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<LibroResponseDTO>> encontrarPorTitulo(@PathVariable String titulo){

        List<LibroResponseDTO> librosEncontrados = libroServiceExternal.encontrarLibroPorTitulo(titulo);

        return ResponseEntity.ok(librosEncontrados);

    }

    @GetMapping("/autor/{autor}")
    public ResponseEntity<List<LibroResponseDTO>> encontrarPorAutor(@PathVariable String autor){

        List<LibroResponseDTO> librosEncontrados = libroServiceExternal.encontrarLibroPorAutor(autor);

        return ResponseEntity.ok(librosEncontrados);

    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<LibroResponseDTO> encontrarPorIsbn(@PathVariable String isbn){

        LibroResponseDTO libroEncontrado = libroServiceExternal.encontrarLibroPorIsbn(isbn);

        return ResponseEntity.ok(libroEncontrado);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<LibroResponseDTO>> encontrarPorCategoria(@PathVariable String categoria){

        List<LibroResponseDTO> librosEncontrados = libroServiceExternal.encontrarLibroPorCategoria(categoria);

        return ResponseEntity.ok(librosEncontrados);

    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<LibroResponseDTO>> encontarPorCategoria(@PathVariable String estado){

        List<LibroResponseDTO> librosEncontrados = libroServiceExternal.encontrarLibroPorEstado(estado);

        return ResponseEntity.ok(librosEncontrados);
    }

}
