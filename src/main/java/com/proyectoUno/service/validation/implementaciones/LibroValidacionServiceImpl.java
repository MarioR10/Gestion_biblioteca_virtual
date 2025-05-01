package com.proyectoUno.service.validation.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.exception.EntidadDuplicadaException;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.exception.ListaDeEntidadesVaciaException;
import com.proyectoUno.service.validation.interfaces.LibroValidacionService;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class LibroValidacionServiceImpl implements LibroValidacionService {


    @Override
    public void validarDatosActualizacion(Libro datosActualizacion) {

        // Validar que al menos un campo viene para actualizar
        if (Stream.of(
                        datosActualizacion.getTitulo(),
                        datosActualizacion.getAutor(),
                        datosActualizacion.getCategoria(),
                        datosActualizacion.getAnioDePublicacion())
                .allMatch(Objects::isNull)) {
            throw new ValidationException("Debe proporcionar al menos un campo para actualizar");
        }

    }


    @Override
    public void validarDisponibilidadDelLibro(Libro libro){

        //verificar si esta disponible
        if (!libro.getEstado().equals("disponible")) {

            throw new RuntimeException("El libro con ID: " + libro.getId() + " no está disponible");

        }

    }
}
