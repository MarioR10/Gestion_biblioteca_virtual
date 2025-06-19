package com.proyectoUno.service.Internal.interfaces;

import com.proyectoUno.dto.reponse.PrestamoResponseDTO;
import com.proyectoUno.dto.request.prestamo.PrestamoCrearRequestDTO;
import com.proyectoUno.entity.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PrestamoService {


    PrestamoResponseDTO encontrarPrestamoPorId(UUID id);
    void marcarPrestamoComoDevuelto(Prestamo prestamo);
    void crearPrestamo(PrestamoCrearRequestDTO prestamoCrearRequestDTO);
    void registrarDevolucion(UUID prestamoId);
    Prestamo encontrarPrestamoPorIdInternal(UUID id);
    Page<PrestamoResponseDTO> encontrarPrestamosActivosPorUsuarios(UUID usuarioId, Pageable pageable);
    Page<PrestamoResponseDTO> encontrarHistorialDePrestamoPorUsuario(UUID usuarioId, Pageable pageable);
    Page<PrestamoResponseDTO> encontrarPrestamosActivos(Pageable pageable);
    Page<PrestamoResponseDTO> encontrarPrestamos(Pageable pageable);


}
