package com.proyectoUno.service.External.implementaciones;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.maper.libro.LibroRequestMapper;
import com.proyectoUno.maper.libro.LibroResponseMapper;
import com.proyectoUno.repository.LibroRepository;
import com.proyectoUno.service.External.interfaces.LibroServiceExternal;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class LibroServiceExternalImpl implements LibroServiceExternal {

    private static final Logger log = (Logger) LoggerFactory.getLogger(LibroServiceExternal.class);
    private final LibroResponseMapper libroResponseMapper;
    private final LibroRequestMapper libroRequestMapper;
    private final LibroRepository libroRepository;






    @Autowired
    public LibroServiceExternalImpl(LibroRepository libroRepository, LibroResponseMapper libroResponseMapper, LibroRequestMapper libroRequestMapper){

        this.libroRepository=libroRepository;
        this.libroResponseMapper = libroResponseMapper;
        this.libroRequestMapper = libroRequestMapper;
    }

    @Override
    public List<LibroResponseDTO> encontrarLibros() {

        //Obtener la lista de libros
        List<Libro> libros=libroRepository.findAll();

        //Verificar que la lista no este vacia
        if (libros.isEmpty()) {

            //Si esta vacia mandamos mandamos mensaje
            log.warning("No hay libros disponibles en este momento");  // Mensaje de advertencia
        }

        //Convertimos lista Libros a DTO
        return  libroResponseMapper.convertirAListaResponseDTO(libros);
    }

    @Override
    public LibroResponseDTO  encontrarLibroPorId(UUID theid) {

        //Verificamos que se encuentre la entidad dentro del Optional, si no esta tiramos excepcion
        Libro libro = libroRepository.findById(theid)
                .orElseThrow(() -> new EntidadNoEncontradaException("El libro con ID " + theid + " no ha sido encontrado"));


        //Convertimos Libro a DTO
        return libroResponseMapper.convertirAResponseDTO(libro);
    }

    @Override
    @Transactional
    public void eliminarLibroPorId(UUID theid) {

        libroRepository.deleteById(theid);
    }

    @Override
    @Transactional
    public LibroResponseDTO actualizarLibro(LibroActualizarRequestDTO actualizarLibroRequestDTO) {

        //Encontramos el libro (es decir verificamos que exista algo para actualizar)

        Libro libroEncontrado= libroRepository.findById(actualizarLibroRequestDTO.getId())
                .orElseThrow(()-> new EntidadNoEncontradaException("El libro con ID " + actualizarLibroRequestDTO.getId() + " no ha sido encontrado"));

        //Asignamos nuevos valores y actualizamos con los datos del DTO
        Libro libroActualizado= libroRequestMapper.actualizarEntidadDesdeDTO(actualizarLibroRequestDTO, libroEncontrado);

        //Convertimos entidad a DTO y retonamos respuesta
        return libroResponseMapper.convertirAResponseDTO(libroActualizado);

    }

    @Override
    @Transactional
    public void guardarLibro(LibroCrearRequestDTO libroCrearRequestDTO) {
        Libro libro= libroRequestMapper.crearEntidadDesdeDTO(libroCrearRequestDTO);
         libroRepository.save(libro);
    }

    @Override
    public List<LibroResponseDTO> encontrarLibroPorTitulo(String titulo) {

        //Lista de Entidad Libro
        List <Libro>  libros = libroRepository.findLibroByTituloContaining(titulo);

        //verificamos si la lista esta vacia

        if (libros.isEmpty()) {
            throw new EntidadNoEncontradaException("Libros no encontrados con el título: " + titulo);
        }
        //Convertimos a lista de tipo LibroResponseDTO

        return libroResponseMapper.convertirAListaResponseDTO(libros);
    }

    @Override
    public List<LibroResponseDTO> encontrarLibroPorAutor(String autor) {

        //Lista de Entidad Libro
        List <Libro> libros = libroRepository.findAllByAutorContaining(autor);

        //verificamos si fueron encontrados

        if (libros.isEmpty()) {
            throw new EntidadNoEncontradaException("Libros no encontrados con el autor: " + autor);
        }

            //Convertimos a lista de tipo LibroResponseDTO
            return libroResponseMapper.convertirAListaResponseDTO(libros);

    }

    @Override
    public  LibroResponseDTO encontrarLibroPorIsbn(String isbn) {

        //Encuentra el libro (Entidad)

        Libro libro = libroRepository.findLibroByIsbn(isbn)
                .orElseThrow(()-> new EntidadNoEncontradaException("El libro con Isbn " + isbn + " no ha sido encontrado"));

        //Convierte a DTO
        return libroResponseMapper.convertirAResponseDTO(libro);

    }


    @Override
    public List<LibroResponseDTO> encontrarLibroPorCategoria(String categoria) {

        //Encuentra  lista de libros (Entidad)
        List <Libro> libros = libroRepository.findLibroByCategoriaContaining(categoria);

        if (libros.isEmpty()) {
            throw new RuntimeException("Libros no encontrados con la categoria: " + categoria);
        }

        //Convierte Lista Libro a Lista DTO
        return libroResponseMapper.convertirAListaResponseDTO(libros);

    }

    @Override
    public List<LibroResponseDTO> encontrarLibroPorEstado(String estado) {

        //Encuentra  lista de libros (Entidad)
        List<Libro> libros = libroRepository.findByEstado(estado);

        if (libros.isEmpty()) {
            throw new RuntimeException("Libros no encontrados con el estado: " + estado);
        }

        return libroResponseMapper.convertirAListaResponseDTO(libros);
    }


}
