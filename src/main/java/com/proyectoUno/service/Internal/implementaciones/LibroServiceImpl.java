package com.proyectoUno.service.Internal.implementaciones;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.maper.libro.LibroRequestMapperStruct;
import com.proyectoUno.maper.libro.LibroResponseMapperStruct;
import com.proyectoUno.repository.LibroRepository;
import com.proyectoUno.service.Internal.interfaces.LibroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final LibroResponseMapperStruct libroResponseMapper;
    private final LibroRequestMapperStruct libroRequestMapper;



    public LibroServiceImpl(LibroRepository libroRepository, LibroResponseMapperStruct libroResponseMapper, LibroRequestMapperStruct libroRequestMapper){
        this.libroRepository=libroRepository;

        this.libroResponseMapper = libroResponseMapper;
        this.libroRequestMapper = libroRequestMapper;
    }

    //metodos actuales

    @Override
    public LibroResponseDTO encontrarLibroPorId(UUID id){

        //Encontramos el libro en la base de datos
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() ->  new EntidadNoEncontradaException(
                        "Libro",
                        "id",
                        id
                ));
        //Convertimos
        return  libroResponseMapper.toResponseDTO(libro);

    }

    @Override
    @Transactional
    public void eliminarLibroPorId(UUID id) {

        //Encontrmos libro por su Id o lanzamos excepcion
        Libro libro = this.encontrarLibroPorIdInternal(id);

        // Eliminamos libro si existe
        libroRepository.delete(libro);
    }


    @Override
    @Transactional
    public LibroResponseDTO actualizarLibro(UUID id, LibroActualizarRequestDTO libroActualizar) {

        return null;
    }

    @Override
    @Transactional
    public void crearLibro(List<LibroCrearRequestDTO> libroCrearRequestDTO) {

        //Convertimos la lista a Libro
        List<Libro> libros = libroRequestMapper.toEntityList(libroCrearRequestDTO);
        //Guardamos el libro
        libroRepository.saveAll(libros);
    }

    //Metodos paginados




    //Metodos nuevos con paginacion incluida

    @Override
    public Page<LibroResponseDTO> encontrarLibros(Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findAll(pageable);

        //Convertimos
        return libroResponseMapper.toResponseDTOPage(libros);
    }

    @Override
    public Page<LibroResponseDTO> encontrarLibroPorTitulo(String titulo, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findByTituloContaining(titulo, pageable);

        //Convertimos
        return  libroResponseMapper.toResponseDTOPage(libros);
    }

    @Override
    public Page<LibroResponseDTO> encontrarLibroPorAutor(String autor, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findAllByAutorContaining(autor, pageable);

        //Convertimos
        return libroResponseMapper.toResponseDTOPage(libros);
    }

    @Override
    public Page<LibroResponseDTO> encontrarLibroPorIsbn(String isbn, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findLibroByIsbnContaining(isbn, pageable);

        //Convertimos
        return libroResponseMapper.toResponseDTOPage(libros);
    }

    @Override
    public Page<LibroResponseDTO> encontrarLibroPorCategoria(String categoria, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findLibroByCategoriaContaining(categoria, pageable);

        //Convertimos
        return libroResponseMapper.toResponseDTOPage(libros);
    }

    @Override
    public Page<LibroResponseDTO> encontrarLibroPorEstado(String estado, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findLibroByEstadoContaining(estado,pageable);

        //Convertimos
        return libroResponseMapper.toResponseDTOPage(libros);
    }

    @Override
    public Libro encontrarLibroPorIdInternal(UUID id) {

        //Encontrar El Optional en la base de datos o lanzar excepcion
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() ->  new EntidadNoEncontradaException(
                        "Libro",
                        "id",
                        id
                ));

        return libro;
    }


    @Override
    @Transactional
    public void marcarLibroComoPrestado(Libro libro){
        //Asiganmos que el libro ha sido prestado
        libro.setEstado("reservado");
    }

    @Override
    @Transactional
    public void marcarLibroComoDisponible(Libro libro){
        //Asignamos disponible
        libro.setEstado("disponible");
    }

}
