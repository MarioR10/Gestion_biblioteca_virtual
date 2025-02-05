package com.proyectoUno.service.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.repository.LibroRepository;
import com.proyectoUno.service.interfaces.LibroService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LibroServiceImpl implements LibroService {
    private LibroRepository libroRepository;


    @Autowired
    public LibroServiceImpl(LibroRepository libroRepository){

        this.libroRepository=libroRepository;

    }

    @Override
    public List<Libro> encontrarLibros() {

        List<Libro> libros=libroRepository.findAll();
        //verificamos si fueron encontrados

        if (libros.isEmpty()) {
            throw new RuntimeException("No se encontraron Libros" );
        }

        return  libros;
    }

    @Override
    public Optional <Libro>  encontrarLibroPorId(UUID theid) {

        Optional<Libro> libro=libroRepository.findById(theid);

        if(libro.isPresent()){

            return libro;
        } else{
            throw  new RuntimeException("El libro con ID"+ theid+ " no ha sido encontrado");

        }

    }

    @Override
    @Transactional
    public void eliminarLibroPorId(UUID theid) {

        libroRepository.deleteById(theid);
    }

    @Override
    @Transactional
    public Libro actualizarLibro(Libro libro) {

        // Verificamos si el libro existe usando findById, que devuelve un Optional
        Optional<Libro> libroExistente = libroRepository.findById(libro.getId());

        // Si el libro no existe, lanzamos una excepción
        libroExistente.orElseThrow(() ->
                new RuntimeException("El libro con ID " + libro.getId() + " no ha sido encontrado")
        );

        //guarda el usuario actualizado

        return libroRepository.save(libro);
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
            throw new RuntimeException("Libros no encontrados con el título: " + titulo);
        }


        return libros;
    }

    @Override
    public List<Libro> encontrarLibroPorAutor(String autor) {

        List <Libro> libros = libroRepository.findAllByAutorContaining(autor);

        //verificamos si fueron encontrados

        if (libros.isEmpty()) {
            throw new RuntimeException("Libros no encontrados con el autor: " + autor);
        }
        else {
            return libros;

        }
    }

    @Override
    public  Optional<Libro> encontrarLibroPorIsbn(String isbn) {

        Optional<Libro> libro = libroRepository.findLibroByIsbn(isbn);

        if( libro.isPresent()){

            return libro;

        }else {

            throw  new RuntimeException("El libro con isbn"+ isbn+ " no ha sido encontrado");
        }

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
