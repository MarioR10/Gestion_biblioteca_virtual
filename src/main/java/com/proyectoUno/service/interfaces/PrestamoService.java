package com.proyectoUno.service.interfaces;

import com.proyectoUno.entity.Prestamo;

import java.util.List;
import java.util.UUID;

public interface PrestamoService {

    // Crear prestamo
    Prestamo crearPrestamo(UUID usuarioId, UUID libroId);


    //Consultar prestamos activos por ususario
    List<Prestamo> encontrarPrestamosActivosPorUsuarios(UUID usuarioId);

    //registrar devolucion de un libro
    void registrarDevoluvion(UUID prestamo);

    //Obtener historial de prestamos de un usuario

    List <Prestamo> obtenerHistorialDePrestamoPorUsuario( UUID usuarioId);


}
