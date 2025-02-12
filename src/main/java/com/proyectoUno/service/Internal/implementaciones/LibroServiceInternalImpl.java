package com.proyectoUno.service.Internal.implementaciones;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.entity.Libro;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.repository.LibroRepository;
import com.proyectoUno.service.Internal.interfaces.LibroServiceInternal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class LibroServiceInternalImpl implements LibroServiceInternal {

    private LibroRepository libroRepository;

    @Autowired
    public LibroServiceInternalImpl(LibroRepository libroRepository){

        this.libroRepository=libroRepository;
    }
    @Override
    public Libro encontrarLibroPorId(UUID theid) {

        //Verificamos que se encuentre la entidad dentro del Optional, si no esta tiramos excepcion
        Libro libro = libroRepository.findById(theid)
                .orElseThrow(() -> new EntidadNoEncontradaException("El libro con ID " + theid + " no ha sido encontrado"));


        //Convertimos Libro a DTO
        return libro;

    }

    @Override
    @Transactional
    public void actualizarLibro(Libro libro) {

       libroRepository.save(libro);


    }

}
