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
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class LibroValidacionServiceImpl implements LibroValidacionService {

    @Override
    public void validarListaDeLibrosNoVacia(List<Libro> libros){

        if (libros== null || libros.isEmpty()){

            throw new ListaDeEntidadesVaciaException("No se encontraron libros en la base de datos");
        }
    }

    @Override
    public Libro validarLibroExistencia(Optional<Libro> libroOptional) {
        Libro libro= libroOptional.orElseThrow(
                () -> new EntidadNoEncontradaException("El libro buscado no ha sido encontrado"));

        return libro;

    }

    @Override
    public void validarDatosActualizacion(Libro datosActualizacion) {

        // Validar que al menos un campo viene para actualizar
        if (Stream.of(
                        datosActualizacion.getTitulo(),
                        datosActualizacion.getAutor(),
                        datosActualizacion.getCategoria())
                .allMatch(Objects::isNull)) {
            throw new ValidationException("Debe proporcionar al menos un campo para actualizar");
        }

    }
    @Override
    public void validarLibroNoDuplicado(Optional<Libro> libro, String isbn) {
        if (libro.isPresent()){

           throw new EntidadDuplicadaException("Ya existe un libro con isbn: "+ isbn);
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
