package com.proyectoUno.service.validation.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Prestamo;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.exception.ListaDeEntidadesVaciaException;
import com.proyectoUno.service.validation.interfaces.PrestamoValidacionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrestamoValidacionServiceImpl implements PrestamoValidacionService {
    @Override
    public void validarListaDePrestamosNoVacia(List<Prestamo> prestamos) {

        if (prestamos== null || prestamos.isEmpty()){
            throw new ListaDeEntidadesVaciaException("No se encontraron prestamos en la base de datos");
        }

    }

    @Override
    public Prestamo validarPrestamoExistencia(Optional<Prestamo> prestamoOptional) {

        Prestamo prestamo= prestamoOptional.orElseThrow(
                () -> new EntidadNoEncontradaException("El Prestamo buscado no ha sido encontrado"));

        return prestamo;
    }
    @Override
    public void validarEstadoDelPrestamo(String estado){

        if(!estado.equals("activo")) {
            throw new RuntimeException("Estado no válido: " + estado);
        }

    }

}
