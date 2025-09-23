package com.proyectoUno.service.implementaciones;

import com.proyectoUno.dto.reponse.PrestamoResponseDTO;
import com.proyectoUno.dto.request.prestamo.PrestamoCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Prestamo;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.ConflictException;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.maper.prestamo.PrestamoResponseMapperStruct;
import com.proyectoUno.repository.PrestamoRepository;
import com.proyectoUno.service.interfaces.LibroService;
import com.proyectoUno.service.interfaces.PrestamoService;
import com.proyectoUno.service.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Servicio encargado de gestionar todos los procesos relacionados con préstamos de libros.
 * Incluye:
 * 1. Creación de préstamos.
 * 2. Registro de devoluciones.
 * 3. Búsqueda de préstamos activos o históricos.
 * 4. Gestión del estado del préstamo y del libro asociado.
 */
@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final UsuarioService usuarioService;
    private final LibroService libroService;


    private final PrestamoRepository prestamoRepository;
    private final PrestamoResponseMapperStruct prestamoResponseMapper;

    /**
     * Constructor que inyecta las dependencias necesarias.
     * @param usuarioService          Servicio para operaciones sobre usuarios.
     * @param libroService            Servicio para operaciones sobre libros.
     * @param prestamoRepository      Repositorio JPA para acceder a los datos de préstamos.
     * @param prestamoResponseMapper  Mapper para convertir entidades Prestamo a DTOs de respuesta.
     */
    @Autowired
    public PrestamoServiceImpl(UsuarioService usuarioService, LibroService libroService, PrestamoRepository prestamoRepository, PrestamoResponseMapperStruct prestamoResponseMapper) {
        this.usuarioService = usuarioService;
        this.libroService = libroService;
        this.prestamoRepository = prestamoRepository;
        this.prestamoResponseMapper = prestamoResponseMapper;
    }

    /**
     * Busca un préstamo por su ID.
     * @param id Identificador del préstamo.
     * @return DTO con la información del préstamo.
     * @throws EntidadNoEncontradaException Si el préstamo no existe.
     */
    @Override
    public PrestamoResponseDTO encontrarPrestamoPorId(UUID id) {
        //Validamos el prestamo

        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Prestamo","ID",id) );

        //retornamos
        return prestamoResponseMapper.toResponseDTO(prestamo);
    }


    /**
     * Crea un nuevo préstamo.
     * Verifica la disponibilidad del libro y actualiza su estado a "reservado".
     * @param prestamoCrearRequestDTO DTO con la información del préstamo a crear.
     * @throws ConflictException Si el libro no se encuentra disponible.
     */
    @Override
    public void crearPrestamo(PrestamoCrearRequestDTO prestamoCrearRequestDTO) {

        //encontramos el usuario  y libro a quien estara asociado el prestamo

        Usuario usuario = usuarioService.encontrarUsuarioPorIdInterno(prestamoCrearRequestDTO.idUsuario());
        Libro libro = libroService.encontrarLibroPorIdInternal(prestamoCrearRequestDTO.idLibro());

        //Verificamos disponibilidad del libro
        if ( !libro.getEstado().equals("disponible")){
            throw new ConflictException("El libro no se encuentra disponible","Libro","ID", libro.getId());
        }

        //Crear Prestamo
        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);
        prestamo.setFechaDevolucion(LocalDateTime.now().plusDays(15));
        prestamoRepository.save(prestamo);

        //actualizamos el estado del libro
        libroService.marcarLibroComoPrestado(libro);

    }

    /**
     * Registra la devolución de un préstamo.
     * Valida que el préstamo siga activo y actualiza el estado tanto del préstamo como del libro.
     * @param id Identificador del préstamo.
     * @throws ConflictException Si el préstamo no se encuentra activo.
     */
    @Override
    public void registrarDevolucion(UUID id) {

        //Verificamos la existencia del prestamo
        Prestamo prestamo = encontrarPrestamoPorIdInternal(id);

        //Validamos que el prestamo siga activo
        if(!prestamo.getEstado().equals("activo")) {
            throw new ConflictException("El prestamo no se encuentra activo", "Libro", "ID", prestamo.getId());
        }

        //Actualizamos el estado del prestamo a devuelto
        marcarPrestamoComoDevuelto(prestamo);

        //Actualizamos el estado del libro a disponible
        libroService.marcarLibroComoDisponible(prestamo.getLibro());

    }

    /**
     * Obtiene los préstamos activos de un usuario específico de forma paginada.
     * @param usuarioId ID del usuario.
     * @param pageable  Información de paginación.
     * @return Página de DTOs de préstamos activos.
     */
    @Override
    public Page<PrestamoResponseDTO> encontrarPrestamosActivosPorUsuarios(UUID usuarioId, Pageable pageable) {

        //Encontramos prestamos activos del usuario  en la base de datos
        Page<Prestamo> prestamos = prestamoRepository.findPrestamoByUsuarioIdAndEstado(usuarioId, "activo", pageable);

        //convertimos
        return  prestamoResponseMapper.toResponseDTOPage(prestamos);

    }

    // ==========================================
    // MÉTODOS CON PAGINACIÓN
    // ==========================================


    /**
     * Obtiene el historial completo de préstamos de un usuario de forma paginada.
     * @param usuarioId ID del usuario.
     * @param pageable  Información de paginación.
     * @return Página de DTOs con todos los préstamos del usuario.
     */
    @Override
    public Page<PrestamoResponseDTO> encontrarHistorialDePrestamoPorUsuario(UUID usuarioId, Pageable pageable) {

        //Encontramos todos los prestamos del usuario
        Page<Prestamo> prestamos = prestamoRepository.findByUsuarioId(usuarioId, pageable);

        //convertimos
        return  prestamoResponseMapper.toResponseDTOPage(prestamos);

    }

    /**
     * Obtiene todos los préstamos activos del sistema de forma paginada.
     * @param pageable Información de paginación.
     * @return Página de DTOs de préstamos activos.
     */
    @Override
    public Page<PrestamoResponseDTO> encontrarPrestamosActivos(Pageable pageable) {

        //Buscar todos los prestamos activos de la DB
        Page<Prestamo> prestamos = prestamoRepository.findByEstado("activo",pageable);

        //convertir
        return  prestamoResponseMapper.toResponseDTOPage(prestamos);

    }

    /**
     * Obtiene todos los préstamos de forma paginada.
     * @param pageable Información de paginación.
     * @return Página de DTOs con todos los préstamos.
     */
    @Override
    public Page<PrestamoResponseDTO> encontrarPrestamos(Pageable pageable){

        //Encontrar prestamos
        Page<Prestamo> prestamos = prestamoRepository.findAll(pageable);

        //Convertir

        return prestamoResponseMapper.toResponseDTOPage(prestamos);
    }

    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================

    /**
     * Busca un préstamo por ID y devuelve la entidad directamente.
     * Útil para operaciones internas donde se requiere la entidad completa.
     * @param id Identificador del préstamo.
     * @return Entidad Prestamo.
     * @throws EntidadNoEncontradaException Si no se encuentra el préstamo.
     */
    @Override
    public Prestamo encontrarPrestamoPorIdInternal(UUID id) {
        //Validamos el prestamo

        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Prestamo","ID",id) );

        //retornamos
        return prestamo;
    }

    /**
     * Marca un préstamo como devuelto (estado = "finalizado").
     * @param prestamo Entidad préstamo a actualizar.
     */
    @Override
    @Transactional
    public void marcarPrestamoComoDevuelto(Prestamo prestamo) {
        prestamo.setEstado("finalizado");
    }

    }