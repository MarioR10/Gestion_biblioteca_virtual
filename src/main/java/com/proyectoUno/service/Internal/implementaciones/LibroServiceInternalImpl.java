package com.proyectoUno.service.Internal.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.exception.SolicitudActualizacionInvalidaException;
import com.proyectoUno.repository.LibroRepository;
import com.proyectoUno.service.Internal.interfaces.LibroServiceInternal;
import com.proyectoUno.service.validation.interfaces.LibroValidacionService;
import com.proyectoUno.service.validation.interfaces.ValidacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class LibroServiceInternalImpl implements LibroServiceInternal {

    private LibroRepository libroRepository;
    private LibroValidacionService libroValidacionService;
    private ValidacionService validacionService;


    public LibroServiceInternalImpl(LibroRepository libroRepository, LibroValidacionService libroValidacionService, ValidacionService validacionService){
        this.libroRepository=libroRepository;
        this.libroValidacionService=libroValidacionService;
        this.validacionService=validacionService;
    }

    //metodos actuales

    @Override
    public Libro encontrarLibroPorId(UUID id) {

        //Encontrar El Optional en la base de datos o lanzar excepcion
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() ->  new EntidadNoEncontradaException(
                        "Libro",
                        "id",
                        id
                ));

        return libro;
    }

    @Override
    @Transactional
    public void eliminarLibroPorId(UUID id) {

        //Encontrmos libro por su Id o lanzamos excepcion
       Libro libro = encontrarLibroPorId(id);

       // Eliminamos libro si existe
       libroRepository.delete(libro);
    }

    @Override
    @Transactional
    public Libro actualizarLibro(UUID id, Libro datosActualizacion) {

        // Validar que al menos un campo viene para actualizar

        /* Stream.of(..) crea un flujo de datos, .all, allMatch(...)
        Resvisa cada uno de los elementos del flujo de datos, devuelve true si
        todos los elementos son nulll, y false si al menos un elemento del flujo
        no es null
         */

        if (Stream.of(
                        datosActualizacion.getTitulo(),
                        datosActualizacion.getAutor(),
                        datosActualizacion.getCategoria(),
                        datosActualizacion.getAnioDePublicacion())
                .allMatch(Objects::isNull)) {
            throw new SolicitudActualizacionInvalidaException("Libro");
        }

        //Encontramos (buscamos) la entidad que si esta presente en la base de datos con todos sus campos
        Libro libroExistente= encontrarLibroPorId(id);

        //Actualizar la entidad existente con la entidad parcial
        libroExistente.setTitulo(datosActualizacion.getTitulo());
        libroExistente.setAutor(datosActualizacion.getAutor());
        libroExistente.setCategoria(datosActualizacion.getCategoria());
        libroExistente.setAnioDePublicacion(datosActualizacion.getAnioDePublicacion());
        return libroExistente;
    }

    @Override
    @Transactional
    public void crearLibro(List<Libro> libros) {

        //Guardamos el libro
        libroRepository.saveAll(libros);
    }

    @Override
    @Transactional
    public void marcarLibroComoPrestado(Libro libro){
        //Asiganmos que el libro ha sido prestado
        libro.setEstado("reservado");
    }

    @Override
    @Transactional
    public void marcarLibroComoDisponible(Libro libro){
        //Asignamos disponible
        libro.setEstado("disponible");
    }

    //Metodos nuevos con paginacion incluida

    @Override
    public Page<Libro> encontrarLibros(Pageable pageable) {

        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findAll(pageable);
        return libros;
    }

    @Override
    public Page<Libro> encontrarLibroPorTitulo(String titulo, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findByTituloContaining(titulo, pageable);
        return  libros;
    }

    @Override
    public Page<Libro> encontrarLibroPorAutor(String autor, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findAllByAutorContaining(autor, pageable);
        return libros;
    }

    @Override
    public Page<Libro> encontrarLibroPorIsbn(String isbn, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findLibroByIsbnContaining(isbn, pageable);
        return libros;
    }

    @Override
    public Page<Libro> encontrarLibroPorCategoria(String categoria, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findLibroByCategoriaContaining(categoria, pageable);
        return libros;
    }

    @Override
    public Page<Libro> encontrarLibroPorEstado(String estado, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findLibroByEstadoContaining(estado,pageable);
        return libros;
    }

}
