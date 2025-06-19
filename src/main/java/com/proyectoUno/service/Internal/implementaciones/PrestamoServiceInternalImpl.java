package com.proyectoUno.service.Internal.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Prestamo;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.repository.PrestamoRepository;
import com.proyectoUno.service.Internal.interfaces.PrestamoServiceIternal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PrestamoServiceInternalImpl implements PrestamoServiceIternal {

    private PrestamoRepository prestamoRepository;

    @Autowired
    public PrestamoServiceInternalImpl(PrestamoRepository prestamoRepository){

        this.prestamoRepository=prestamoRepository;
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
    public Prestamo encontrarPrestamoPorId(UUID id) {
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

    @Override
    public Page<Prestamo> encontrarPrestamosPorIdUsuario(UUID id, Pageable pageable) {

        Page<Prestamo> prestamosUsuario = prestamoRepository.findByUsuarioId(id,pageable);
        return prestamosUsuario;
    }

    @Override
    public Page<Prestamo> encontrarPrestamosActivosPorIdUsuario(UUID id, Pageable pageable) {
        Page<Prestamo> prestamosAcivos = prestamoRepository.findPrestamoByUsuarioIdAndEstado(id,"activo", pageable);
        return prestamosAcivos;
    }

    @Override
    public Page<Prestamo> encontrarPrestamosActivos(Pageable pageable) {
        Page<Prestamo> prestamosActivos = prestamoRepository.findByEstado("activo",pageable);

        return prestamosActivos;
    }

    @Override
    public Page<Prestamo> encontrarPrestamos(Pageable pageable) {

        Page<Prestamo> prestamos= prestamoRepository.findAll(pageable);
        return prestamos;
    }


}
