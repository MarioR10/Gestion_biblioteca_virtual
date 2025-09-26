package com.proyectoUno.apisExternas.controller;
import com.proyectoUno.apisExternas.dto.ExternalBookDTO;
import com.proyectoUno.apisExternas.mapper.ExternalBookMapper;
import com.proyectoUno.apisExternas.service.ExternalBookService;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.service.interfaces.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * RestController de Spring que gestiona solicitudes HTTP relacionadas con libros,
 * obteniendo datos desde una API externa y delegando la creación en el servicio interno.
 */
@RestController
@RequestMapping("/api/external/libro")
public class ExternalBookController {

    private final ExternalBookService externalBookService;
    private final LibroService libroService;
    private final ExternalBookMapper externalBookMapper;

    @Autowired
    public ExternalBookController(ExternalBookService externalBookService, LibroService libroService, ExternalBookMapper externalBookMapper) {
        this.externalBookService = externalBookService;
        this.libroService = libroService;
        this.externalBookMapper = externalBookMapper;
    }

    /**
     * Método se encarga de agrega un libro al sistema a partir de su ISBN consultando una API externa.
     * Luego delega la creación del libro al servicio interno de la aplicación.
     * @param isbn ISBN del libro a consultar en la API externa.
     */
    @PostMapping("/agregar")
    public void agregarLibro(@RequestParam String isbn){

        // Obtiene los detalles del libro desde la API externa usando el ISBN
        ExternalBookDTO libroApi = externalBookService.obtenerDetallesDelLibro(isbn);

        // Convierte los datos externos al DTO interno para poder persistirlos en la base de datos
        LibroCrearRequestDTO libro = externalBookMapper.toLibroCrearRequestDTO(libroApi);

        // Persiste el libro en el sistema usando el servicio interno
        libroService.crearLibro(libro);
    }
}
