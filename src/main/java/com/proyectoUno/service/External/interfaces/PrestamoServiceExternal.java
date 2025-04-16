package com.proyectoUno.service.External.interfaces;

import com.proyectoUno.dto.reponse.PrestamoResponseDTO;
import com.proyectoUno.dto.request.prestamo.PrestamoCrearRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PrestamoServiceExternal {

    // Crear prestamo
    void crearPrestamo(PrestamoCrearRequestDTO  prestamoCrearRequestDTO);

    //Consultar prestamos activos por ususario
    List<PrestamoResponseDTO> encontrarPrestamosActivosPorUsuarios(UUID usuarioId);

    //Encontrar prestamo por Id
    PrestamoResponseDTO encontrarPrestamoPorId(UUID id);
    //registrar devolucion de un libro
    void registrarDevolucion(UUID prestamo);

    //Obtener historial de prestamos de un usuario
    List <PrestamoResponseDTO> encontrarHistorialDePrestamoPorUsuario(UUID usuarioId);


    //metodos paginados
    Page<PrestamoResponseDTO> encontrarPrestamos(Pageable pageable);
    Page<PrestamoResponseDTO>encontrarPrestamosActivosPorUsuarios(UUID usuarioId, Pageable pageable);
    Page<PrestamoResponseDTO> encontrarHistorialDePrestamoPorUsuario(UUID usuarioId, Pageable pageable);
    Page<PrestamoResponseDTO> encontrarPrestamosActivos(Pageable pageable);
}
