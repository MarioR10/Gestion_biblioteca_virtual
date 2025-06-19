package com.proyectoUno.service.External.implementaciones;

import com.proyectoUno.dto.reponse.PrestamoResponseDTO;
import com.proyectoUno.dto.request.prestamo.PrestamoCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Prestamo;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.ConflictException;
import com.proyectoUno.maper.prestamo.PrestamoResponseMapperStruct;
import com.proyectoUno.service.External.interfaces.PrestamoServiceExternal;
import com.proyectoUno.service.Internal.interfaces.LibroServiceInternal;
import com.proyectoUno.service.Internal.interfaces.PrestamoServiceIternal;
import com.proyectoUno.service.Internal.interfaces.UsuarioServiceInternal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrestamoServiceExternalImpl implements PrestamoServiceExternal {

    private final UsuarioServiceInternal usuarioServiceInternal;
    private final PrestamoResponseMapperStruct prestamoResponseMapper;
    private final PrestamoServiceIternal prestamoServiceIternal;
    private LibroServiceInternal libroServicesInternal;



    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public PrestamoServiceExternalImpl(LibroServiceInternal libroServicesInternal, UsuarioServiceInternal usuarioServiceInternal,
                                       PrestamoResponseMapperStruct prestamoResponseMapper, PrestamoServiceIternal prestamoServiceIternal){

        this.libroServicesInternal=libroServicesInternal;
        this.usuarioServiceInternal = usuarioServiceInternal;
        this.prestamoResponseMapper = prestamoResponseMapper;
        this.prestamoServiceIternal = prestamoServiceIternal;
    }
    @Override
    public void crearPrestamo(PrestamoCrearRequestDTO prestamoCrearRequestDTO) {

        //Encontramos el usuario
        Usuario usuario = usuarioServiceInternal.encontrarUsuarioPorId(prestamoCrearRequestDTO.idUsuario());

        //Encontramos el libro
        Libro libro = libroServicesInternal.encontrarLibroPorId(prestamoCrearRequestDTO.idLibro());

        //verificar si el libro esta disponible

        if (!libro.getEstado().equals("disponible")) {

            throw new ConflictException("El libro no se encuentra disponible","Libro","ID", libro.getId());
        }

        //Crear Prestamo
        prestamoServiceIternal.crearPrestamo(libro,usuario);

        //Actualizar estado del libro
        libroServicesInternal.marcarLibroComoPrestado(libro);

    }

    @Override

    public PrestamoResponseDTO encontrarPrestamoPorId(UUID id){

        //obtenemos prestamo
        Prestamo prestamo = prestamoServiceIternal.encontrarPrestamoPorId(id);

        //converitmos a DTO
        return prestamoResponseMapper.toResponseDTO(prestamo);
    }
    @Override
    public void registrarDevolucion(UUID prestamoId) {

        //Verificamos si el prestamo existe
        Prestamo prestamo = prestamoServiceIternal.encontrarPrestamoPorId(prestamoId);

        //Validamos que el prestamo este activo aun
        if(!prestamo.getEstado().equals("activo")) {
            throw new ConflictException("El prestamo no se encuentra activo", "Libro", "ID", prestamo.getId());
        }


        //Actualizamos el estado del prestamo a devuelto
        prestamoServiceIternal.marcarPrestamoComoDevuelto(prestamo);
        //guardamos los datos

        //Actualizamos el estado del libro a disponible
        libroServicesInternal.marcarLibroComoDisponible(prestamo.getLibro());

    }



    //Metodos paginados
    @Override
    public Page<PrestamoResponseDTO> encontrarPrestamosActivosPorUsuarios(UUID usuarioId, Pageable pageable) {

        Page<Prestamo> prestamosActivos = prestamoServiceIternal.encontrarPrestamosActivosPorIdUsuario(usuarioId, pageable);
        return prestamoResponseMapper.toResponseDTOPage(prestamosActivos);
    }

    @Override
    public Page<PrestamoResponseDTO> encontrarHistorialDePrestamoPorUsuario(UUID usuarioId, Pageable pageable) {
        Page<Prestamo> prestamosPorUsuario= prestamoServiceIternal.encontrarPrestamosPorIdUsuario(usuarioId,pageable);
        return prestamoResponseMapper.toResponseDTOPage(prestamosPorUsuario);
    }

    @Override
    public Page<PrestamoResponseDTO> encontrarPrestamosActivos(Pageable pageable) {

        Page<Prestamo> prestamosActivos = prestamoServiceIternal.encontrarPrestamosActivos(pageable);

        return prestamoResponseMapper.toResponseDTOPage(prestamosActivos);
    }

    @Override
    public Page<PrestamoResponseDTO> encontrarPrestamos(Pageable pageable) {

        Page<Prestamo> prestamos = prestamoServiceIternal.encontrarPrestamos(pageable);

        return prestamoResponseMapper.toResponseDTOPage(prestamos);
    }
}
