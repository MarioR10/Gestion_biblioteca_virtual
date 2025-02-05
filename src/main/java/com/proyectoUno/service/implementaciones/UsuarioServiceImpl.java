package com.proyectoUno.service.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.repository.UsuarioRepository;
import com.proyectoUno.service.interfaces.UsuarioService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

        List<Usuario> usuarios= usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
            throw new RuntimeException("Usuarios no encontrados con el estado");
        } else {
            return usuarios;
        }

    }

    @Override
    public Optional <Usuario>  encontrarUsuarioPorId(UUID theid) {

    Optional <Usuario> usuario= usuarioRepository.findById(theid);

    if(usuario.isPresent()){
        return usuario;

    }else {
        throw  new RuntimeException("El usuasrio con id: "+ theid+ " no ha sido encontrado");
    }


    }

    @Override
    public void eliminarUsuarioPorId(UUID theid) {

        usuarioRepository.deleteById(theid);
    }

    @Override
    public Usuario actualizarUsuario(Usuario usuario) {

        //Verificamos si el usuario existe
       Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());

        // Si el usuario no existe, lanzamos una excepción

        usuarioExistente.orElseThrow(() ->
                new RuntimeException("El usuario con ID: "+usuario.getId()+ " no ha sido encontrado")
        );


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

        Optional <Usuario> usuarioOptional= usuarioRepository.findById(theid);

        Usuario usuario= null;

        if(usuarioOptional.isPresent())

            usuario= usuarioOptional.get();

        else {
           throw  new RuntimeException("Usuario con ID " + theid + " no encontrado");
        }


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

        Optional <Usuario> usuarioOptional= usuarioRepository.findById(theid);

        Usuario usuario= null;

        if(usuarioOptional.isPresent())

            usuario= usuarioOptional.get();

        else {
            throw  new RuntimeException("Usuario con ID " + theid + " no encontrado");
        }

        //cambiar rol
        usuario.setActivo(estado);

        //Guardamos
        return usuarioRepository.save(usuario);
    }

}
