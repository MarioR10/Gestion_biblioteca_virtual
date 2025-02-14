package com.proyectoUno.service.External.interfaces;

import com.proyectoUno.dto.reponse.PrestamoResponseDTO;
import com.proyectoUno.dto.request.prestamo.PrestamoCrearRequestDTO;

import java.util.List;
import java.util.UUID;

public interface PrestamoServiceExternal {

    // Crear prestamo
    void guardarPrestamo(PrestamoCrearRequestDTO prestamoCrearRequestDTO);


    //Consultar prestamos activos por ususario
    List<PrestamoResponseDTO> encontrarPrestamosActivosPorUsuarios(UUID usuarioId,String estado);

    //registrar devolucion de un libro
    void registrarDevolucion(UUID prestamo);

    //Obtener historial de prestamos de un usuario

    List <PrestamoResponseDTO> obtenerHistorialDePrestamoPorUsuario( UUID usuarioId);


}
