package com.proyectoUno.service.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.repository.UsuarioRepository;
import com.proyectoUno.service.interfaces.UsuarioService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {


    private UsuarioRepository usuarioRepository;



    //Inyeccion de dependencias
    @Autowired
    public  UsuarioServiceImpl(UsuarioRepository usuarioRepository){

        this.usuarioRepository=usuarioRepository;
    }

    @Override
    public List<Usuario> encontrarUsuarios() {

        List<Usuario> usuarios= usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
            throw new RuntimeException("Usuarios no encontrados con el estado");
        } else {
            return usuarios;
        }

    }

    @Override
    public Usuario  encontrarUsuarioPorId(UUID theid) {

        //Verificamos que se encuentre la entidad dentro del Optional, si no esta tiramos excepcion
    return  usuarioRepository.findById(theid)
            .orElseThrow(()-> new EntidadNoEncontradaException("El Usuario con ID: "+theid+ " no ha sido encontrado"));

    }

    @Override
    @Transactional
    public void eliminarUsuarioPorId(UUID theid) {

        usuarioRepository.deleteById(theid);
    }

    @Override
    @Transactional
    public Usuario actualizarUsuario(Usuario usuario) {

        //Encontramos el Usuario a actualizar

        Usuario usuarioEncontrado = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El usuario con ID: " +usuario.getId()+ " no ha sido encontrado"));

        //Actualizamos el Usuario
        usuarioEncontrado.setRol(usuario.getRol());
        usuarioEncontrado.setActivo(usuario.getActivo());
        usuarioEncontrado.setEmail(usuario.getEmail());

        //guarda el usuario actualizado
        return usuarioEncontrado; // JPA actualizará automáticamente por @Transactional


    }

    @Override
    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario cambiarRol(UUID theid, String nuevoRol) {

        //Encontramos el usuario

        Usuario usuario = usuarioRepository.findById(theid)
                .orElseThrow(()-> new EntidadNoEncontradaException("El usuario con ID: "+ theid+ " no ha sido encontrado"));

        if(!nuevoRol.equals("admin") && !nuevoRol.equals("usuario")){
            throw new RuntimeException("Rol no válido: " + nuevoRol);
        }

        //Actualizamos el rol al usuario
        usuario.setRol(nuevoRol);

       // guardamos los cambios
        return  usuario; // JPA actualizará automáticamente por @Transactional
    }

    @Override
    @Transactional
    public Usuario cambiarEstadoUsuario(UUID theid,boolean estado) {

        //Encontramos el usuario

        Usuario usuario = usuarioRepository.findById(theid)
                .orElseThrow(()-> new EntidadNoEncontradaException("El usuario con ID: "+ theid+ " no ha sido encontrado"));


        //Actualizamos el rol del usuario
        usuario.setActivo(estado);

        //Guardamos
        return usuario; // JPA actualizará automáticamente por @Transactional
    }

}
