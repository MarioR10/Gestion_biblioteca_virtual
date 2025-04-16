package com.proyectoUno.service.External.implementaciones;

import com.proyectoUno.dto.reponse.PrestamoResponseDTO;
import com.proyectoUno.dto.request.prestamo.PrestamoCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Prestamo;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.maper.prestamo.PrestamoResponseMapper;
import com.proyectoUno.repository.PrestamoRepository;
import com.proyectoUno.service.External.interfaces.LibroServiceExternal;
import com.proyectoUno.service.External.interfaces.PrestamoServiceExternal;
import com.proyectoUno.service.External.interfaces.UsuarioServiceExternal;
import com.proyectoUno.service.Internal.interfaces.LibroServiceInternal;
import com.proyectoUno.service.Internal.interfaces.PrestamoServiceIternal;
import com.proyectoUno.service.Internal.interfaces.UsuarioServiceInternal;
import com.proyectoUno.service.validation.interfaces.LibroValidacionService;
import com.proyectoUno.service.validation.interfaces.PrestamoValidacionService;
import com.proyectoUno.service.validation.interfaces.ValidacionService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class PrestamoServiceExternalImpl implements PrestamoServiceExternal {

    private final UsuarioServiceInternal usuarioServiceInternal;
    private final PrestamoResponseMapper prestamoResponseMapper;
    private final LibroValidacionService libroValidacionService;
    private final PrestamoServiceIternal prestamoServiceIternal;
    private final PrestamoValidacionService prestamoValidacionService;
    private final ValidacionService validacionService;
    private LibroServiceInternal libroServicesInternal;



    @Autowired
    public PrestamoServiceExternalImpl(LibroServiceInternal libroServicesInternal, UsuarioServiceInternal usuarioServiceInternal, PrestamoResponseMapper prestamoResponseMapper, LibroValidacionService libroValidacionService, PrestamoServiceIternal prestamoServiceIternal, PrestamoValidacionService prestamoValidacionService, ValidacionService validacionService){

        this.libroServicesInternal=libroServicesInternal;
        this.usuarioServiceInternal = usuarioServiceInternal;
        this.prestamoResponseMapper = prestamoResponseMapper;
        this.libroValidacionService = libroValidacionService;
        this.prestamoServiceIternal = prestamoServiceIternal;
        this.prestamoValidacionService = prestamoValidacionService;
        this.validacionService = validacionService;
    }
    @Override
    public void crearPrestamo(PrestamoCrearRequestDTO prestamoCrearRequestDTO) {

        //Encontramos el usuario
        Usuario usuario = usuarioServiceInternal.encontrarUsuarioPorId(prestamoCrearRequestDTO.getIdUsuario());

        //Encontramos el libro
        Libro libro = libroServicesInternal.encontrarLibroPorId(prestamoCrearRequestDTO.getIdLibro());

        //verificar si el libro esta disponible
        libroValidacionService.validarDisponibilidadDelLibro(libro);

        //Crear Prestamo
        prestamoServiceIternal.crearPrestamo(libro,usuario);

        //Actualizar estado del libro
        libroServicesInternal.marcarLibroComoPrestado(libro);

    }
    @Override
    public List<PrestamoResponseDTO> encontrarPrestamosActivosPorUsuarios(UUID usuarioId) {

      //Encontramos prestamos
        List <Prestamo> prestamos= prestamoServiceIternal.encontrarPrestamosActivosPorIdUsuario(usuarioId);

     //Convertimos a DTO
       return prestamoResponseMapper.convertirAListaResponseDTO(prestamos);
    }

    @Override

    public PrestamoResponseDTO encontrarPrestamoPorId(UUID id){

        //obtenemos prestamo
        Prestamo prestamo = prestamoServiceIternal.encontrarPrestamoPorId(id);

        //converitmos a DTO
        return prestamoResponseMapper.convertirAresponseDTO(prestamo);
    }
    @Override
    public void registrarDevolucion(UUID prestamoId) {

        //Verificamos si el prestamo existe
        Prestamo prestamo = prestamoServiceIternal.encontrarPrestamoPorId(prestamoId);

        //Validamos que el prestamo este activo aun
        prestamoValidacionService.validarEstadoDelPrestamo(prestamo.getEstado());

        //Actualizamos el estado del prestamo a devuelto
        prestamoServiceIternal.marcarPrestamoComoDevuelto(prestamo);
        //guardamos los datos

        //Actualizamos el estado del libro a disponible
        libroServicesInternal.marcarLibroComoDisponible(prestamo.getLibro());

    }
    @Override
    public List<PrestamoResponseDTO> encontrarHistorialDePrestamoPorUsuario(UUID usuarioId) {

        //Encontrar el historial de prestamos
        List<Prestamo> prestamos = prestamoServiceIternal.encontrarPrestamosPorIdUsuario(usuarioId);

        //Convertir a DTO
        return prestamoResponseMapper.convertirAListaResponseDTO(prestamos);
    }

    @Override
    public Page<PrestamoResponseDTO> encontrarPrestamos(Pageable pageable) {

        Page<Prestamo> prestamos= prestamoServiceIternal.encontrarPrestamos(pageable);

        return prestamoResponseMapper.convertirAPageResponseDTO(prestamos);
    }

    //Metodos paginados
    @Override
    public Page<PrestamoResponseDTO> encontrarPrestamosActivosPorUsuarios(UUID usuarioId, Pageable pageable) {

        Page<Prestamo> prestamosActivos = prestamoServiceIternal.encontrarPrestamosActivosPorIdUsuario(usuarioId, pageable);
        validacionService.validarPaginaNoVacia(prestamosActivos, "Prestamos");
        return prestamoResponseMapper.convertirAPageResponseDTO(prestamosActivos);
    }

    @Override
    public Page<PrestamoResponseDTO> encontrarHistorialDePrestamoPorUsuario(UUID usuarioId, Pageable pageable) {
        Page<Prestamo> prestamosPorUsuario= prestamoServiceIternal.encontrarPrestamosPorIdUsuario(usuarioId,pageable);
        validacionService.validarPaginaNoVacia(prestamosPorUsuario, "Prestamos");
        return prestamoResponseMapper.convertirAPageResponseDTO(prestamosPorUsuario);
    }

    @Override
    public Page<PrestamoResponseDTO> encontrarPrestamosActivos(Pageable pageable) {

        Page<Prestamo> prestamosActivos = prestamoServiceIternal.encontrarPrestamosActivos(pageable);

        return prestamoResponseMapper.convertirAPageResponseDTO(prestamosActivos);
    }
}
