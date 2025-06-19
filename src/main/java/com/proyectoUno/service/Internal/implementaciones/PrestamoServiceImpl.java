package com.proyectoUno.service.Internal.implementaciones;

import com.proyectoUno.dto.reponse.PrestamoResponseDTO;
import com.proyectoUno.dto.request.prestamo.PrestamoCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Prestamo;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.ConflictException;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.maper.prestamo.PrestamoResponseMapperStruct;
import com.proyectoUno.repository.PrestamoRepository;
import com.proyectoUno.service.Internal.interfaces.LibroService;
import com.proyectoUno.service.Internal.interfaces.PrestamoService;
import com.proyectoUno.service.Internal.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final UsuarioService usuarioService;
    private final LibroService libroService;


    private final PrestamoRepository prestamoRepository;
    private final PrestamoResponseMapperStruct prestamoResponseMapper;


    @Autowired
    public PrestamoServiceImpl(UsuarioService usuarioService, LibroService libroService, PrestamoRepository prestamoRepository, PrestamoResponseMapperStruct prestamoResponseMapper) {
        this.usuarioService = usuarioService;
        this.libroService = libroService;
        this.prestamoRepository = prestamoRepository;
        this.prestamoResponseMapper = prestamoResponseMapper;
    }

    @Override
    public PrestamoResponseDTO encontrarPrestamoPorId(UUID id) {
        //Validamos el prestamo

        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Prestamo","ID",id) );

        //retornamos
        return prestamoResponseMapper.toResponseDTO(prestamo);
    }



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

    @Override
    public Page<PrestamoResponseDTO> encontrarPrestamosActivosPorUsuarios(UUID usuarioId, Pageable pageable) {

        //Encontramos prestamos activos del usuario  en la base de datos
        Page<Prestamo> prestamos = prestamoRepository.findPrestamoByUsuarioIdAndEstado(usuarioId, "activo", pageable);

        //convertimos
        return  prestamoResponseMapper.toResponseDTOPage(prestamos);

    }

    @Override
    public Page<PrestamoResponseDTO> encontrarHistorialDePrestamoPorUsuario(UUID usuarioId, Pageable pageable) {

        //Encontramos todos los prestamos del usuario
        Page<Prestamo> prestamos = prestamoRepository.findByUsuarioId(usuarioId, pageable);

        //convertimos
        return  prestamoResponseMapper.toResponseDTOPage(prestamos);

    }

    @Override
    public Page<PrestamoResponseDTO> encontrarPrestamosActivos(Pageable pageable) {

        //Buscar todos los prestamos activos de la DB
        Page<Prestamo> prestamos = prestamoRepository.findByEstado("activo",pageable);

        //convertir
        return  prestamoResponseMapper.toResponseDTOPage(prestamos);

    }


    @Override
    public Page<PrestamoResponseDTO> encontrarPrestamos(Pageable pageable){

        //Encontrar prestamos
        Page<Prestamo> prestamos = prestamoRepository.findAll(pageable);

        //Convertir

        return prestamoResponseMapper.toResponseDTOPage(prestamos);
    }

    //internal
    @Override
    public Prestamo encontrarPrestamoPorIdInternal(UUID id) {
        //Validamos el prestamo

        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Prestamo","ID",id) );

        //retornamos
        return prestamo;
    }

    @Override
    @Transactional
    public void marcarPrestamoComoDevuelto(Prestamo prestamo) {
        prestamo.setEstado("finalizado");
    }

    }