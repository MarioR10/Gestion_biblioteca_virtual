package com.proyectoUno.service.validation.interfaces;

import com.proyectoUno.entity.Prestamo;


import java.util.List;
import java.util.Optional;

public interface PrestamoValidacionService {

    void validarListaDePrestamosNoVacia(List<Prestamo> prestamos);

    Prestamo validarPrestamoExistencia(Optional<Prestamo> prestamos);

    void validarEstadoDelPrestamo(String estado);


}
