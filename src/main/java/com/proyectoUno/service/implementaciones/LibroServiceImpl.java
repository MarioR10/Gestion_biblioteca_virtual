package com.proyectoUno.service.implementaciones;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.maper.libro.LibroRequestMapperStruct;
import com.proyectoUno.maper.libro.LibroResponseMapperStruct;
import com.proyectoUno.repository.LibroRepository;
import com.proyectoUno.service.interfaces.LibroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Servicio responsable de manejar todas las operaciones relacionadas con libros.
 * Incluye:
 * 1. CRUD básico (crear, leer, actualizar, eliminar).
 * 2. Búsquedas con paginación por título, autor, ISBN, categoría y estado.
 * 3. Gestión del estado de los libros (prestado o disponible).
 */
@Service
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final LibroResponseMapperStruct libroResponseMapper;
    private final LibroRequestMapperStruct libroRequestMapper;


    /**
     * Constructor que inyecta las dependencias necesarias.
     * @param libroRepository       Repositorio JPA para acceder a los datos de libros.
     * @param libroResponseMapper   Mapper para convertir entidades Libro a DTOs de respuesta.
     * @param libroRequestMapper    Mapper para convertir DTOs de solicitud a entidades Libro.
     */
    public LibroServiceImpl(LibroRepository libroRepository, LibroResponseMapperStruct libroResponseMapper, LibroRequestMapperStruct libroRequestMapper){
        this.libroRepository=libroRepository;

        this.libroResponseMapper = libroResponseMapper;
        this.libroRequestMapper = libroRequestMapper;
    }


    /**
     * Busca un libro por su ID.
     * @param id Identificador único del libro.
     * @return DTO con la información del libro.
     * @throws EntidadNoEncontradaException Si el libro no existe en la base de datos.
     */
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

    /**
     * Elimina un libro por su ID.
     * @param id Identificador único del libro.
     * @throws EntidadNoEncontradaException Si el libro no existe.
     */
    @Override
    @Transactional
    public void eliminarLibroPorId(UUID id) {

        //Encontrmos libro por su Id o lanzamos excepcion
        Libro libro = this.encontrarLibroPorIdInternal(id);

        // Eliminamos libro si existe
        libroRepository.delete(libro);
    }

    /**
     * Actualiza un libro existente con los datos proporcionados.
     * Solo se actualizan los campos no nulos del DTO.
     * @param id Identificador del libro a actualizar.
     * @param libroActualizar DTO con los campos a actualizar.
     * @return DTO actualizado con la información final del libro.
     * @throws EntidadNoEncontradaException Si el libro no existe.
     */
    @Override
    @Transactional
    public LibroResponseDTO actualizarLibro(UUID id, LibroActualizarRequestDTO libroActualizar) {

        //1. Buscamos el libro en la base de datos
        Libro libro = encontrarLibroPorIdInternal(id);

        //2. Usar MapStruct para actualizar los campos no null del DTO, en la entidad ya existente
        libroRequestMapper.toUpdateEntity(libroActualizar, libro);

        //3. Persistimos en la base de datos la entidad actualizada
       Libro libroGuardado= libroRepository.save(libro);

        //convertimos
        return  libroResponseMapper.toResponseDTO(libroGuardado);
    }

    /**
     * Crea una lista de libros en la base de datos.
     * @param libroCrearRequestDTO Lista de DTOs con la información de los libros a crear.
     */
    @Override
    @Transactional
    public void crearLibro(List<LibroCrearRequestDTO> libroCrearRequestDTO) {

        //Convertimos la lista a Libro
        List<Libro> libros = libroRequestMapper.toEntityList(libroCrearRequestDTO);
        //Guardamos el libro
        libroRepository.saveAll(libros);
    }

    // ==========================================
    // MÉTODOS CON PAGINACIÓN
    // ==========================================

    /**
     * Obtiene todos los libros de forma paginada.
     * @param pageable Información de la página solicitada (número de página, tamaño, ordenamiento).
     * @return Página de DTOs de libros.
     */
    @Override
    public Page<LibroResponseDTO> encontrarLibros(Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findAll(pageable);

        //Convertimos
        return libroResponseMapper.toResponseDTOPage(libros);
    }

    /**
     * Busca libros por título (contiene) de forma paginada.
     */
    @Override
    public Page<LibroResponseDTO> encontrarLibroPorTitulo(String titulo, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findByTituloContaining(titulo, pageable);

        //Convertimos
        return  libroResponseMapper.toResponseDTOPage(libros);
    }

    /**
     * Busca libros por autor (contiene) de forma paginada.
     */
    @Override
    public Page<LibroResponseDTO> encontrarLibroPorAutor(String autor, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findAllByAutorContaining(autor, pageable);

        //Convertimos
        return libroResponseMapper.toResponseDTOPage(libros);
    }

    /**
     * Busca libros por ISBN (contiene) de forma paginada.
     */
    @Override
    public Page<LibroResponseDTO> encontrarLibroPorIsbn(String isbn, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findLibroByIsbnContaining(isbn, pageable);

        //Convertimos
        return libroResponseMapper.toResponseDTOPage(libros);
    }

    /**
     * Busca libros por categoría (contiene) de forma paginada.
     */
    @Override
    public Page<LibroResponseDTO> encontrarLibroPorCategoria(String categoria, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findLibroByCategoriaContaining(categoria, pageable);

        //Convertimos
        return libroResponseMapper.toResponseDTOPage(libros);
    }

    /**
     * Busca libros por estado (contiene) de forma paginada.
     */
    @Override
    public Page<LibroResponseDTO> encontrarLibroPorEstado(String estado, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findLibroByEstadoContaining(estado,pageable);

        //Convertimos
        return libroResponseMapper.toResponseDTOPage(libros);
    }

    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================

    /**
     * Busca un libro por ID y devuelve la entidad directamente.
     * Útil para operaciones internas donde necesitamos la entidad completa.
     * @param id Identificador del libro.
     * @return Entidad Libro.
     * @throws EntidadNoEncontradaException Si no se encuentra el libro.
     */
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

    /**
     * Marca un libro como prestado (estado = "reservado").
     * @param libro Entidad libro a actualizar.
     */
    @Override
    @Transactional
    public void marcarLibroComoPrestado(Libro libro){
        //Asiganmos que el libro ha sido prestado
        libro.setEstado("reservado");
    }

    /**
     * Marca un libro como disponible (estado = "disponible").
     *
     * @param libro Entidad libro a actualizar.
     */
    @Override
    @Transactional
    public void marcarLibroComoDisponible(Libro libro){
        //Asignamos disponible
        libro.setEstado("disponible");
    }

    @Override
    @Transactional
    public void crearLibro(LibroCrearRequestDTO libroCrearRequestDTO) {

        Libro libro = libroRequestMapper.toEntity(libroCrearRequestDTO);
        libroRepository.save(libro);
    }
}
