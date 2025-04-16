package com.proyectoUno.service.External.implementaciones;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import com.proyectoUno.maper.libro.LibroRequestMapper;
import com.proyectoUno.maper.libro.LibroResponseMapper;
import com.proyectoUno.service.External.interfaces.LibroServiceExternal;
import com.proyectoUno.service.Internal.interfaces.LibroServiceInternal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class LibroServiceExternalImpl implements LibroServiceExternal {

    private final LibroResponseMapper libroResponseMapper;
    private final LibroRequestMapper libroRequestMapper;

    private final LibroServiceInternal libroServiceInternal;


    @Autowired
    public LibroServiceExternalImpl(LibroResponseMapper libroResponseMapper, LibroRequestMapper libroRequestMapper, LibroServiceInternal libroServiceInternal){
        this.libroResponseMapper = libroResponseMapper;
        this.libroRequestMapper = libroRequestMapper;
        this.libroServiceInternal = libroServiceInternal;
    }

    @Override
    public LibroResponseDTO  encontrarLibroPorId(UUID id) {
        //Encontramos libro
        Libro libro = libroServiceInternal.encontrarLibroPorId(id);
        //Convertimos Libro a DTO
        return libroResponseMapper.convertirAResponseDTO(libro);
    }

    @Override
    public void eliminarLibroPorId(UUID id) {
        libroServiceInternal.eliminarLibroPorId(id);
    }

    @Override
    public LibroResponseDTO actualizarLibro(UUID id,LibroActualizarRequestDTO requestDTO) {
        //Obtenemos la entidad parcial (al crearla solo le asignamos los campos del DTO), esta no se agurda en la BD
        Libro datosActualizacion= libroRequestMapper.actualizarEntidadDesdeDTO(requestDTO);
        //Pasamosla entidad parcial con sus datos, a la propia entidad, para signarle los nuevos valores de dihcos campos
        Libro libroActualizado = libroServiceInternal.actualizarLibro(id,datosActualizacion);
        //Retonornamos el reponse ya con los datos actualizados
        return libroResponseMapper.convertirAResponseDTO(libroActualizado);

    }
    @Override

    public void crearLibro(List<LibroCrearRequestDTO> libroCrearRequestDTO) {
        //Convertir  lista DTO en entidad
        List<Libro> libros = libroRequestMapper.convertirAListaEntidad(libroCrearRequestDTO);
        //Guardamos la entidad
         libroServiceInternal.crearLibro(libros);
    }

    @Override
    public Page<LibroResponseDTO> encontrarLibros(Pageable pageable) {
        //encontramos los libros
        Page<Libro> libros = libroServiceInternal.encontrarLibros(pageable);
        //Convertir a DTO
        return libroResponseMapper.convertirAPageResponseDTO(libros);
    }

    @Override
    public Page<LibroResponseDTO> encontrarLibroPorTitulo(String titulo, Pageable pageable) {

        //Encontramos los libros
        Page<Libro> libros =libroServiceInternal.encontrarLibroPorTitulo(titulo,pageable);
        //Convertir a DTO
        return libroResponseMapper.convertirAPageResponseDTO(libros);
    }

    @Override
    public Page<LibroResponseDTO> encontrarLibroPorAutor(String autor, Pageable pageable) {
        //Encontramos los libros
        Page<Libro> libros =libroServiceInternal.encontrarLibroPorAutor(autor,pageable);
        //Convertir a DTO
        return libroResponseMapper.convertirAPageResponseDTO(libros);
    }

    @Override
    public Page<LibroResponseDTO> encontrarLibroPorIsbn(String isbn, Pageable pageable) {
        //Encontramos los libros
        Page<Libro> libros =libroServiceInternal.encontrarLibroPorIsbn(isbn,pageable);
        //Convertir a DTO
        return libroResponseMapper.convertirAPageResponseDTO(libros);
    }

    @Override
    public Page<LibroResponseDTO> encontrarLibroPorCategoria(String categoria, Pageable pageable) {
        //Encontramos los libros
        Page<Libro> libros =libroServiceInternal.encontrarLibroPorCategoria(categoria,pageable);
        //Convertir a DTO
        return libroResponseMapper.convertirAPageResponseDTO(libros);
    }

    @Override
    public Page<LibroResponseDTO> encontrarLibroPorEstado(String estado, Pageable pageable) {
        //Encontramos los libros
        Page<Libro> libros =libroServiceInternal.encontrarLibroPorEstado(estado,pageable);
        //Convertir a DTO
        return libroResponseMapper.convertirAPageResponseDTO(libros);
    }

}
