package com.proyectoUno.service.Internal.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Prestamo;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.repository.PrestamoRepository;
import com.proyectoUno.service.Internal.interfaces.PrestamoServiceIternal;
import com.proyectoUno.service.validation.interfaces.PrestamoValidacionService;
import com.proyectoUno.service.validation.interfaces.ValidacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PrestamoServiceInternalImpl implements PrestamoServiceIternal {

    private final ValidacionService validacionService;
    private PrestamoRepository prestamoRepository;

    private PrestamoValidacionService prestamoValidacionService;

    @Autowired
    public PrestamoServiceInternalImpl(PrestamoRepository prestamoRepository, PrestamoValidacionService prestamoValidacionService, ValidacionService validacionService){

        this.prestamoRepository=prestamoRepository;
        this.prestamoValidacionService=prestamoValidacionService;
        this.validacionService = validacionService;
    }


    @Override
    @Transactional
    public void crearPrestamo(Libro libro, Usuario usuario) {
        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        prestamo.setUsuario(usuario);
        prestamo.setFechaDevolucion(LocalDateTime.now().plusDays(15));
        prestamoRepository.save(prestamo);
    }

    @Override
    public List<Prestamo> encontrarPrestamosActivosPorIdUsuario(UUID id) {
        List <Prestamo> prestamos = prestamoRepository.findPrestamoByUsuarioIdAndEstado(id,"activo");
        //Verificamos la lista
        prestamoValidacionService.validarListaDePrestamosNoVacia(prestamos);
        return prestamos;
    }

    @Override
    public Prestamo encontrarPrestamoPorId(UUID id) {
        //Encontramos el OptionalPrestamo
        Optional<Prestamo> prestamoOptional = prestamoRepository.findById(id);
        //Validamos el prestamo
        Prestamo prestamo= prestamoValidacionService.validarPrestamoExistencia(prestamoOptional);
       //retornamos
        return prestamo;
    }

    @Override
    public List<Prestamo> encontrarPrestamosPorIdUsuario(UUID id) {
        List < Prestamo> prestamos = prestamoRepository.findByUsuarioId(id);
        //validados la lista de prestamos
        prestamoValidacionService.validarListaDePrestamosNoVacia(prestamos);
        return prestamos;
    }

    @Override
    @Transactional
    public void marcarPrestamoComoDevuelto(Prestamo prestamo) {
        prestamo.setEstado("finalizado");
    }

    @Override
    public Page<Prestamo> encontrarPrestamos(Pageable pageable) {
        Page<Prestamo> prestamos = prestamoRepository.findAll(pageable);
        validacionService.validarPaginaNoVacia(prestamos, "Prestamos");
        return prestamos;
    }
    @Override
    public Page<Prestamo> encontrarPrestamosPorIdUsuario(UUID id, Pageable pageable) {

        Page<Prestamo> prestamosUsuario = prestamoRepository.findByUsuarioId(id,pageable);
        validacionService.validarPaginaNoVacia(prestamosUsuario, "Usuario");
        return prestamosUsuario;
    }

    @Override
    public Page<Prestamo> encontrarPrestamosActivosPorIdUsuario(UUID id, Pageable pageable) {
        Page<Prestamo> prestamosAcivos = prestamoRepository.findPrestamoByUsuarioIdAndEstado(id,"activo", pageable);
        validacionService.validarPaginaNoVacia(prestamosAcivos, "Prestamos");
        return prestamosAcivos;
    }

    @Override
    public Page<Prestamo> encontrarPrestamosActivos(Pageable pageable) {
        Page<Prestamo> prestamosActivos = prestamoRepository.findByEstado("activo",pageable);
        validacionService.validarPaginaNoVacia(prestamosActivos, "Prestamos");

        return prestamosActivos;
    }


}
