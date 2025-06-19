package com.proyectoUno.service.Internal.interfaces;

import com.proyectoUno.dto.reponse.PrestamoResponseDTO;
import com.proyectoUno.dto.request.prestamo.PrestamoCrearRequestDTO;
import com.proyectoUno.entity.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PrestamoServiceIternal {


    void crearPrestamo(PrestamoCrearRequestDTO prestamoCrearRequestDTO);

    //Encontrar prestamo por Id
    PrestamoResponseDTO encontrarPrestamoPorId(UUID id);
    //registrar devolucion de un libro
    void registrarDevolucion(UUID prestamo);

    //metodos paginados
    Page<PrestamoResponseDTO>encontrarPrestamosActivosPorUsuarios(UUID usuarioId, Pageable pageable);
    Page<PrestamoResponseDTO> encontrarHistorialDePrestamoPorUsuario(UUID usuarioId, Pageable pageable);
    Page<PrestamoResponseDTO> encontrarPrestamosActivos(Pageable pageable);
    Page<PrestamoResponseDTO> encontrarPrestamos(Pageable pageable);


    //Metodos actuales
    Prestamo encontrarPrestamoPorIdInternal(UUID id);
    void marcarPrestamoComoDevuelto(Prestamo prestamo);


}
