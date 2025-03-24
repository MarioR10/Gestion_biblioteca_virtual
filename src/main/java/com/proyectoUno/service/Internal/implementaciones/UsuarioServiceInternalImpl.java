package com.proyectoUno.service.Internal.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.repository.UsuarioRepository;
import com.proyectoUno.service.validation.interfaces.UsuarioValidacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceInternalImpl implements com.proyectoUno.service.Internal.interfaces.UsuarioServiceInternal {


    private UsuarioRepository usuarioRepository;
    private UsuarioValidacionService usuarioValidacionService;

    @Autowired
    public UsuarioServiceInternalImpl(UsuarioRepository usuarioRepository, UsuarioValidacionService usuarioValidacionService){

        this.usuarioRepository=usuarioRepository;
        this.usuarioValidacionService=usuarioValidacionService;
    }

    @Override
    public List <Usuario> encontrarUsuarios(){

        //Encontramos los usuarios
        List <Usuario> usuarios = usuarioRepository.findAll();

        //Verificamos la lista
        usuarioValidacionService.validarListaDeUsuariossNoVacia(usuarios);

        return usuarios;
    }

    @Override
    public Usuario encontrarUsuarioPorId(UUID id){

        //Encontramos el optional
        Optional <Usuario> usuarioOptional= usuarioRepository.findById(id);

        //Validamos el usuario
        Usuario usuario= usuarioValidacionService.validarUsuarioExistencia(usuarioOptional);

        return usuario;
    }
    @Override
    @Transactional
    public void eliminarUsuarioPorId(UUID id){

        try {
            usuarioRepository.deleteById(id);

        } catch(EntidadNoEncontradaException e){

            throw new EntidadNoEncontradaException("Usuario no encontrado, no puede ser eliminado");
        }

    }
    @Override
    @Transactional
    public Usuario actualizarUsuario(UUID id, Usuario datosValidar) {

        //Verifica los datos que vienen en la entidad parcial
        usuarioValidacionService.validarDatosActualizacion(datosValidar);

        //Encontramos (buscamos) la entidad que si esta presente en la base de datos con todos sus campos
        Usuario usuarioEncontrado = encontrarUsuarioPorId(id);

        //Actualizamos la entidad

        usuarioEncontrado.setNombre(datosValidar.getNombre());
        usuarioEncontrado.setApellido(datosValidar.getApellido());
        usuarioEncontrado.setEmail(datosValidar.getEmail());
        usuarioEncontrado.setRol(datosValidar.getRol());
        usuarioEncontrado.setActivo(datosValidar.getActivo());

        return  usuarioEncontrado;
    }
    @Override
    @Transactional
    public void guardarUsuario(Usuario usuario){

        //Encontrmaos usuario por email
        Optional <Usuario> usuarioEncontrado = usuarioRepository.findByEmail(usuario.getEmail());

        // Validamos que no exista un Libro con el mismo
        usuarioValidacionService.validarUsuarioNoDuplicado(usuarioEncontrado,usuario.getEmail());

        //Gaurdamos
        usuarioRepository.save(usuario);
    }
    @Override
    public Usuario encontrarUsuarioPorEmail(String email){

        //Encontramos el usuario por email
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(email);

        //Verificamos que este presente en la abse de datos
       Usuario usuario=  usuarioValidacionService.validarUsuarioExistencia(usuarioEncontrado);

        return usuario;
    }

    @Override
    @Transactional
    public Usuario actualizarRolUsuario(UUID id, String rol) {

        //Encontramos al usuario por ID
        Usuario usuario= encontrarUsuarioPorId(id);

        //Validamos los roles enviados
        usuarioValidacionService.validarRolUsuario(rol);

        //Actualizamos rol

        usuario.setRol(rol);

        return usuario;
    }

    @Override
    @Transactional
    public Usuario actualizarEstadoUsuario(UUID id, boolean estado) {

        //Encontramos al usuario por ID
        Usuario usuario= encontrarUsuarioPorId(id);

        //Actualizamos rol
        usuario.setActivo(estado);

        return usuario;
    }


}
