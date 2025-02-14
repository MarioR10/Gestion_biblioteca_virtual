package com.proyectoUno.service.External.implementaciones;

import com.proyectoUno.dto.reponse.LibroResponseDTO;
import com.proyectoUno.dto.request.libro.LibroActualizarRequestDTO;
import com.proyectoUno.dto.request.libro.LibroCrearRequestDTO;
import com.proyectoUno.entity.Libro;
import com.proyectoUno.maper.libro.LibroRequestMapper;
import com.proyectoUno.maper.libro.LibroResponseMapper;
import com.proyectoUno.repository.LibroRepository;
import com.proyectoUno.service.External.interfaces.LibroServiceExternal;
import com.proyectoUno.service.Internal.interfaces.LibroServiceInternal;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class LibroServiceExternalImpl implements LibroServiceExternal {

    private static final Logger log = (Logger) LoggerFactory.getLogger(LibroServiceExternal.class);
    private final LibroResponseMapper libroResponseMapper;
    private final LibroRequestMapper libroRequestMapper;
    private final LibroRepository libroRepository;
    private final LibroServiceInternal libroServiceInternal;


    @Autowired
    public LibroServiceExternalImpl(LibroRepository libroRepository, LibroResponseMapper libroResponseMapper, LibroRequestMapper libroRequestMapper, LibroServiceInternal libroServiceInternal){

        this.libroRepository=libroRepository;
        this.libroResponseMapper = libroResponseMapper;
        this.libroRequestMapper = libroRequestMapper;
        this.libroServiceInternal = libroServiceInternal;
    }

    @Override
    public List<LibroResponseDTO> encontrarLibros() {

        //Obtener la lista de libros
        List<Libro> libros=libroServiceInternal.encontrarLibros();

        //Convertimos lista Libros a DTO
        return  libroResponseMapper.convertirAListaResponseDTO(libros);
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

        //Pasamosos la entidad parcial con sus datos, a la propia entidad, para signarle los nuevos valores de dihcos campos
        Libro libroActualizado = libroServiceInternal.actualizarLibro(id,datosActualizacion);

        //Retonornamos el reponse ya con los datos actualizados
        return libroResponseMapper.convertirAResponseDTO(libroActualizado);

    }
    @Override

    public void guardarLibro(LibroCrearRequestDTO libroCrearRequestDTO) {

        //Convertir DTO en entidad
        Libro libro= libroRequestMapper.crearEntidadDesdeDTO(libroCrearRequestDTO);

        //Guardamos la entidad
         libroServiceInternal.guardarLibro(libro);
    }

    @Override
    public List<LibroResponseDTO> encontrarLibroPorTitulo(String titulo) {

        //Lista de Entidad Libro
        List<Libro> libros= libroServiceInternal.encontrarLibroPorTitulo(titulo);

        //Convertimos a lista de tipo LibroResponseDTO
        return libroResponseMapper.convertirAListaResponseDTO(libros);
    }

    @Override
    public List<LibroResponseDTO> encontrarLibroPorAutor(String autor) {

        //Lista de Entidad Libro
        List <Libro> libros = libroServiceInternal.encontrarLibroPorAutor(autor);

        //Convertimos a lista de tipo LibroResponseDTO
        return libroResponseMapper.convertirAListaResponseDTO(libros);
    }

    @Override
    public  LibroResponseDTO encontrarLibroPorIsbn(String isbn) {

        //Encuentra el libro
            Libro libro = libroServiceInternal.encontrarLibroPorIsbn(isbn);

        //Convierte a DTO
        return libroResponseMapper.convertirAResponseDTO(libro);

    }
    @Override
    public List<LibroResponseDTO> encontrarLibroPorCategoria(String categoria) {

        //Encuentra  lista de libros (Entidad)
        List <Libro> libros = libroServiceInternal.encontrarLibroPorCategoria(categoria);

        //Convierte Lista Libro a Lista DTO
        return libroResponseMapper.convertirAListaResponseDTO(libros);

    }
    @Override
    public List<LibroResponseDTO> encontrarLibroPorEstado(String estado) {

        //Encuentra  lista de libros
        List<Libro> libros = libroServiceInternal.encontrarLibroPorEstado(estado);

        //Convierte Lista Libro a Lista DTO
        return libroResponseMapper.convertirAListaResponseDTO(libros);
    }

}
