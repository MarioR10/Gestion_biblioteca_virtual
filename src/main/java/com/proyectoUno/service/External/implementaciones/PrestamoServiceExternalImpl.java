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
import com.proyectoUno.service.Internal.interfaces.UsuarioServiceInternal;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class PrestamoServiceExternalImpl implements PrestamoServiceExternal {


    private final UsuarioServiceInternal usuarioServiceInternal;
    private final PrestamoResponseMapper prestamoResponseMapper;
    private PrestamoRepository prestamoRepository;
    private UsuarioServiceExternal usuarioService;

    private LibroServiceInternal libroServicesInternal;

    private static final Logger log = (Logger) LoggerFactory.getLogger(LibroServiceExternal.class);


    @Autowired
    public PrestamoServiceExternalImpl(UsuarioServiceExternal usuarioService, LibroServiceInternal libroServicesInternal, PrestamoRepository prestamoRepository, UsuarioServiceInternal usuarioServiceInternal, PrestamoResponseMapper prestamoResponseMapper){

        this.prestamoRepository=prestamoRepository;
        this.usuarioService=usuarioService;

        this.libroServicesInternal=libroServicesInternal;
        this.usuarioServiceInternal = usuarioServiceInternal;
        this.prestamoResponseMapper = prestamoResponseMapper;
    }


    @Override
    @Transactional
    public void guardarPrestamo(PrestamoCrearRequestDTO prestamoCrearRequestDTO) {

        //Verificamos el  Usuario existe
        Usuario usuario = usuarioServiceInternal.encontrarUsuarioPorId(prestamoCrearRequestDTO.getIdUsuario());

        //Verificamos el  Libro existe
        Libro libro = libroServicesInternal.encontrarLibroPorId(prestamoCrearRequestDTO.getIdLibro());

        //verificar si esta disponible
        if (!libro.getEstado().equals("disponible")) {

            throw new RuntimeException("El libro con ID: " + prestamoCrearRequestDTO.getIdLibro() + " no está disponible");

        }

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaDevolucion(LocalDateTime.now().plusDays(15));

        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);

        //Actualizar estado del libro

        libro.setEstado("prestado");

        libroServicesInternal.actualizarLibro(libro.getId(),libro);

    }

    @Override
    public List<PrestamoResponseDTO> encontrarPrestamosActivosPorUsuarios(UUID usuarioId) {

       List<Prestamo> prestamos= prestamoRepository.findPrestamoByUsuarioIdAndEstado(usuarioId, "activo");

        // Verificar si no se encontraron préstamos
        if (prestamos.isEmpty()) {
         log.warning("No se encontraron préstamos activos para el usuario");
        }

       return prestamoResponseMapper.convertirAListaResponseDTO(prestamos);
    }

    @Override
    @Transactional
    public void registrarDevoluvion(UUID prestamoId) {

        //Verificamos si el prestamo existe
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(()-> new EntidadNoEncontradaException("El prestamos con ID: "+prestamoId+ " no ha sido encontrado"));

        //Actualizamos el estado del prestamo
        prestamo.setEstado("devuelto");

        // Actualizar el estado del libro a "disponible"
        Libro libro = prestamo.getLibro();
        libro.setEstado("disponible");


    }

    @Override
    public List<PrestamoResponseDTO> obtenerHistorialDePrestamoPorUsuario(UUID usuarioId) {
        List<Prestamo> prestamos = prestamoRepository.findByUsuarioId(usuarioId);

        // Verificar si no se encontraron préstamos
        if (prestamos.isEmpty()) {
            log.warning("No se encontraron préstamos activos para el usuario");
        }

        return prestamoResponseMapper.convertirAListaResponseDTO(prestamos);
    }
}
