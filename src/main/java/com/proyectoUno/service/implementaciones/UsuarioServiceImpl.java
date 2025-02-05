package com.proyectoUno.service.implementaciones;

import com.proyectoUno.entity.Usuario;
import com.proyectoUno.repository.UsuarioRepository;
import com.proyectoUno.service.interfaces.UsuarioService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements UsuarioService {


    private UsuarioRepository usuarioRepository;

    private EntityManager entityManager;

    //Inyeccion de dependencias
    @Autowired
    public  UsuarioServiceImpl(UsuarioRepository usuarioRepository, EntityManager entityManager){

        this.usuarioRepository=usuarioRepository;
        this.entityManager=entityManager;

    }

    @Override
    public List<Usuario> encontrarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario encontrarUsuarioPorId(UUID theid) {

    //nos devuelve un <Optional>, de esta forma devolvemos un valor si elOptional tiene o mandamos que no se encontro

    return  usuarioRepository.findById(theid)
            .orElseThrow(() -> new RuntimeException("No encontramos el usuario especificado"));
    }

    @Override
    public void eliminarUsuarioPorId(UUID theid) {

        usuarioRepository.deleteById(theid);
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {

        //Verificamos si el usuario existe
        if( !usuarioRepository.existsById(usuario.getId()))
            throw new RuntimeException("Usuario con ID " + usuario.getId() + " no encontrado");

        //guarda el usuario actualizado
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario cambiarRol(UUID theid, String nuevoRol) {

        //Encontramos el usuario
       Usuario usuario = usuarioRepository.findById(theid)
               .orElseThrow(() -> new RuntimeException("Usuario con ID " + theid + " no encontrado"));

        /*
         * Si el nuevo rol no es "admin" ni "usuario", lanza una excepción.
         * Esto se logra mediante dos condiciones que verifican si el rol es diferente de "admin"
         * y diferente de "usuario". Si ambas son verdaderas, la excepción se lanza.
         */

        if(!nuevoRol.equals("admin") && !nuevoRol.equals("usuario")){
            throw new RuntimeException("Rol no válido: " + nuevoRol);
        }

        //cambiar rol

       usuario.setRol(nuevoRol);

       // guardamos los cambios
        return  usuarioRepository.save(usuario);
    }

    @Override
    public Usuario cambiarEstadoUsuario(UUID theid,boolean estado) {

        //Encontramos el usuario
        Usuario usuario = usuarioRepository.findById(theid)
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + theid + " no encontrado"));

        usuario.setActivo(estado);
        return usuarioRepository.save(usuario);
    }

}
