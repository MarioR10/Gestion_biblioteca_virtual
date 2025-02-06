package com.proyectoUno.service.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Prestamo;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.repository.LibroRepository;
import com.proyectoUno.repository.PrestamoRepository;
import com.proyectoUno.repository.UsuarioRepository;
import com.proyectoUno.service.interfaces.LibroService;
import com.proyectoUno.service.interfaces.PrestamoService;
import com.proyectoUno.service.interfaces.UsuarioService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    private final LibroRepository libroRepository;
    private PrestamoRepository prestamoRepository;
    private UsuarioService usuarioService;
    private LibroService libroService;



    @Autowired
    public  PrestamoServiceImpl(UsuarioService usuarioService, LibroService libroService, PrestamoRepository prestamoRepository, LibroRepository libroRepository){

        this.prestamoRepository=prestamoRepository;
        this.usuarioService=usuarioService;
        this.libroService=libroService;
        this.libroRepository = libroRepository;
    }

    @Override
    @Transactional
    public Prestamo crearPrestamo(UUID usuarioId, UUID libroId) {

        //Verificamos el  Usuario existe
        Usuario usuario = usuarioService.encontrarUsuarioPorId(usuarioId).get();

        //Verificamos el  Libro existe
        Libro libro = libroService.encontrarLibroPorId(libroId).get();

        //verificar si esta disponible
        if( !libro.getEstado().equals("disponible")) {

            throw new RuntimeException("El libro con ID: " + libroId + " no está disponible");

        }

        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        prestamo.setLibro(libro);
        prestamo.setFechaDevolucion(LocalDateTime.now().plusDays(15));


        return prestamoRepository.save(prestamo);
    }

    @Override
    public List<Prestamo> encontrarPrestamosActivosPorUsuarios(UUID usuarioId) {

        return prestamoRepository.findPrestamoByUsuarioIdAndEstado(usuarioId, "activo");
    }

    @Override
    @Transactional
    public void registrarDevoluvion(UUID prestamoId) {

        //Verificamos si el prestamo existe
        Prestamo prestamo = prestamoRepository.findById(prestamoId).get();

        //Actualizar
        prestamo.setEstado("devuelto");

        // Actualizar el estado del libro a "disponible"
        Libro libro = prestamo.getLibro();
        libro.setEstado("disponible");
        libroService.actualizarLibro(libro);

        // Guardar los cambios
        prestamoRepository.save(prestamo);
    }

    @Override
    public List<Prestamo> obtenerHistorialDePrestamoPorUsuario(UUID usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId);
    }
}
