package com.proyectoUno.service.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.repository.LibroRepository;
import com.proyectoUno.service.interfaces.LibroService;
import jakarta.persistence.EntityManager;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class LibroServiceImpl implements LibroService {

    private static final Logger log = (Logger) LoggerFactory.getLogger(LibroService.class);
    private LibroRepository libroRepository;


    @Autowired
    public LibroServiceImpl(LibroRepository libroRepository){

        this.libroRepository=libroRepository;

    }

    @Override
    public List<Libro> encontrarLibros() {

        //Obtener la lista de libros
        List<Libro> libros=libroRepository.findAll();

        //Verificar que la lista no este vacia
        if (libros.isEmpty()) {

            //Si esta vacia mandamos mandamos mensaje
            log.info("No hay libros disponibles en este momento");  // Mensaje de advertencia
        }

        //retonrmaos la lista
        return  libros;
    }

    @Override
    public Libro  encontrarLibroPorId(UUID theid) {

        //Verificamos que se encuentre la entidad dentro del Optional, si no esta tiramos excepcion
        return libroRepository.findById(theid)
                .orElseThrow(() -> new EntidadNoEncontradaException("El libro con ID " + theid + " no ha sido encontrado"));

    }

    @Override
    @Transactional
    public void eliminarLibroPorId(UUID theid) {

        libroRepository.deleteById(theid);
    }

    @Override
    @Transactional
    public Libro actualizarLibro(Libro libro) {

        //Encontramos el libro (es decir verificamos que exista algo para actualizar)

        Libro libroEncontrado= libroRepository.findById(libro.getId())
                .orElseThrow(()-> new EntidadNoEncontradaException("El libro con ID " + libro.getId() + " no ha sido encontrado"));

        //Si si fue encontrado (existe) actualizamos los campos

        libroEncontrado.setTitulo(libro.getTitulo());
        libroEncontrado.setAutor(libro.getAutor());
        libroEncontrado.setCategoria(libro.getCategoria());
        libroEncontrado.setAnioDePublicacion(libro.getAnioDePublicacion());


        //Gaurdamos el libro actualizado
        return libroEncontrado; // JPA actualizará automáticamente los cambios, No es necesario save()
    }

    @Override
    @Transactional
    public Libro guardarLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    @Override
    public List<Libro> encontrarLibroPorTitulo(String titulo) {


        List <Libro>  libros = libroRepository.findLibroByTituloContaining(titulo);

        //verificamos si fueron encontrados

        if (libros.isEmpty()) {
            throw new EntidadNoEncontradaException("Libros no encontrados con el título: " + titulo);
        }

        return libros;
    }

    @Override
    public List<Libro> encontrarLibroPorAutor(String autor) {

        List <Libro> libros = libroRepository.findAllByAutorContaining(autor);

        //verificamos si fueron encontrados

        if (libros.isEmpty()) {
            throw new EntidadNoEncontradaException("Libros no encontrados con el autor: " + autor);
        }

            return libros;

    }

    @Override
    public  Libro encontrarLibroPorIsbn(String isbn) {

       return  libroRepository.findLibroByIsbn(isbn)
                .orElseThrow(()-> new EntidadNoEncontradaException("El libro con Isbn " + isbn + " no ha sido encontrado"));


    }


    @Override
    public List<Libro> encontrarLibroPorCategoria(String categoria) {

        List <Libro> libros = libroRepository.findLibroByCategoriaContaining(categoria);

        if (libros.isEmpty()) {
            throw new RuntimeException("Libros no encontrados con la categoria: " + categoria);
        }
        else {
            return libros;

        }

    }

    @Override
    public List<Libro> encontrarLibroPorEstado(String estado) {

        List<Libro> libros = libroRepository.findByEstado(estado);

        if (libros.isEmpty()) {
            throw new RuntimeException("Libros no encontrados con el estado: " + estado);
        } else {
            return libros;

        }
    }


}
