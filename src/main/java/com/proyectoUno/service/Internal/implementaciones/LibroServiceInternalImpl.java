package com.proyectoUno.service.Internal.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.repository.LibroRepository;
import com.proyectoUno.service.Internal.interfaces.LibroServiceInternal;
import com.proyectoUno.service.validation.interfaces.LibroValidacionService;
import com.proyectoUno.service.validation.interfaces.ValidacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LibroServiceInternalImpl implements LibroServiceInternal {

    private LibroRepository libroRepository;
    private LibroValidacionService libroValidacionService;
    private ValidacionService validacionService;

    @Autowired
    public LibroServiceInternalImpl(LibroRepository libroRepository, LibroValidacionService libroValidacionService, ValidacionService validacionService){
        this.libroRepository=libroRepository;
        this.libroValidacionService=libroValidacionService;
        this.validacionService=validacionService;
    }

    //metodos actuales

    @Override
    public Libro encontrarLibroPorId(UUID id) {
        //Encontrar El Optional en la base de datos
        Optional<Libro> libroOptional = libroRepository.findById(id);
        //Validar si existe o no
        Libro libro = validacionService.validarExistencia(libroOptional, "Libro");
        return libro;
    }

    @Override
    @Transactional
    public void eliminarLibroPorId(UUID id) {

       Libro libro = encontrarLibroPorId(id);

       libroRepository.delete(libro);
    }

    @Override
    @Transactional
    public Libro actualizarLibro(UUID id, Libro datosActualizacion) {
        //Obtenemos y validamos los campos de la entidad parcial (estos datos asignaremos a la entidad existente)
        libroValidacionService.validarDatosActualizacion(datosActualizacion);
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

        //Validamos la pagina
        validacionService.validarPaginaNoVacia(libros, "libro");
        return libros;
    }

    @Override
    public Page<Libro> encontrarLibroPorTitulo(String titulo, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findByTituloContaining(titulo, pageable);
        //Validamos la pagina
        validacionService.validarPaginaNoVacia(libros, "libro");
        return libros;
    }

    @Override
    public Page<Libro> encontrarLibroPorAutor(String autor, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findAllByAutorContaining(autor, pageable);
        //Validamos la pagina
        validacionService.validarPaginaNoVacia(libros, "libro");
        return libros;
    }

    @Override
    public Page<Libro> encontrarLibroPorIsbn(String isbn, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findLibroByIsbnContaining(isbn, pageable);
        //Validamos la pagina
        validacionService.validarPaginaNoVacia(libros, "libro");
        return libros;
    }

    @Override
    public Page<Libro> encontrarLibroPorCategoria(String categoria, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findLibroByCategoriaContaining(categoria, pageable);
        //Validamos la pagina
        validacionService.validarPaginaNoVacia(libros, "libro");
        return libros;
    }

    @Override
    public Page<Libro> encontrarLibroPorEstado(String estado, Pageable pageable) {
        //Buscamos en la base de datos
        Page<Libro> libros = libroRepository.findLibroByEstadoContaining(estado,pageable);
        //Validamos la pagina
        validacionService.validarPaginaNoVacia(libros, "libro");
        return libros;
    }

}
