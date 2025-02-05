package com.proyectoUno.service.interfaces;

import com.proyectoUno.entity.Prestamo;

import java.util.List;
import java.util.UUID;

public interface PrestamoService {

    //CRUD prestamos

    //CREAR
    Prestamo crearPrestamo(UUID usuarioId, UUID libroId);

    //LEER
    List<Prestamo> encontrarPrestamos();
    Prestamo encontrarPrestamoPorId(UUID theId);


}
