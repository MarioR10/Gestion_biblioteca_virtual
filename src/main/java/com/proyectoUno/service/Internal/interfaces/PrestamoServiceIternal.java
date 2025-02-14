package com.proyectoUno.service.Internal.interfaces;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Prestamo;
import com.proyectoUno.entity.Usuario;

import java.util.List;
import java.util.UUID;

public interface PrestamoServiceIternal {

    void crearPrestamo(Libro libro, Usuario usuario);

    List<Prestamo> encontrarPrestamosActivosPorIdUsuario(UUID id, String estado);

    Prestamo encontrarPrestamoPorId(UUID id);

    List<Prestamo> encontrarPrestamosPorIdUsuario(UUID id);

    void marcarPrestamoComoDevuelto(Prestamo prestamo);
}
