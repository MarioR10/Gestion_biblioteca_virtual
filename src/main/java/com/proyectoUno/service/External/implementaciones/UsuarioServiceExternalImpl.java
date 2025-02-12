package com.proyectoUno.service.External.implementaciones;

import com.proyectoUno.dto.reponse.UsuarioResponseDTO;
import com.proyectoUno.dto.request.usuario.UsuarioActualizarDTO;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.maper.usuario.UsuarioResponseMapper;
import com.proyectoUno.repository.UsuarioRepository;
import com.proyectoUno.service.External.interfaces.UsuarioServiceExternal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioServiceExternalImpl implements UsuarioServiceExternal {


    private final UsuarioResponseMapper usuarioResponseMapper;
    private UsuarioRepository usuarioRepository;



    //Inyeccion de dependencias
    @Autowired
    public UsuarioServiceExternalImpl(UsuarioRepository usuarioRepository, UsuarioResponseMapper usuarioResponseMapper){

        this.usuarioRepository=usuarioRepository;
        this.usuarioResponseMapper = usuarioResponseMapper;
    }

    @Override
    public List<UsuarioResponseDTO> encontrarUsuarios() {

        List<Usuario> usuarios= usuarioRepository.findAll();

        if (usuarios.isEmpty()) {
            throw new RuntimeException("Usuarios no encontrados con el estado");
        }
            return usuarioResponseMapper.convertirAListaResponseDTO(usuarios);
    }

    @Override
    public UsuarioResponseDTO  encontrarUsuarioPorId(UUID theid) {

        //Verificamos que se encuentre la entidad dentro del Optional, si no esta tiramos excepcion
    Usuario usuario=  usuarioRepository.findById(theid)
            .orElseThrow(()-> new EntidadNoEncontradaException("El Usuario con ID: "+theid+ " no ha sido encontrado"));

        return usuarioResponseMapper.convertirAResponseDTO(usuario);
    }

    @Override
    @Transactional
    public void eliminarUsuarioPorId(UUID theid) {

        usuarioRepository.deleteById(theid);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO actualizarUsuario( UsuarioActualizarDTO usuarioActualizar) {

        //Encontramos el Usuario a actualizar

        Usuario usuarioEncontrado = usuarioRepository.findById(usuarioActualizar.getId())
                .orElseThrow(() -> new EntidadNoEncontradaException("El usuario con ID: " +usuarioActualizar.getId()+ " no ha sido encontrado"));

        //Actualizamos el Usuario
        usuarioEncontrado.setRol(usuarioActualizar.getRol());
        usuarioEncontrado.setEmail(usuarioActualizar.getEmail());

        //guarda el usuario actualizado
        return usuarioResponseMapper.convertirAResponseDTO(usuarioEncontrado); // JPA actualizará automáticamente por @Transactional


    }

    @Override
    @Transactional
    public void guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO cambiarRol(UUID theid, String nuevoRol) {

        //Encontramos el usuario
        Usuario usuario = usuarioRepository.findById(theid)
                .orElseThrow(()-> new EntidadNoEncontradaException("El usuario con ID: "+ theid+ " no ha sido encontrado"));

        if(!nuevoRol.equals("admin") && !nuevoRol.equals("usuario")){
            throw new RuntimeException("Rol no válido: " + nuevoRol);
        }

        //Actualizamos el rol al usuario
        usuario.setRol(nuevoRol);

       // guardamos los cambios
        return  usuarioResponseMapper.convertirAResponseDTO(usuario); // JPA actualizará automáticamente por @Transactional
    }

    @Override
    @Transactional
    public UsuarioResponseDTO cambiarEstadoUsuario(UUID theid,boolean estado) {

        //Encontramos el usuario

        Usuario usuario = usuarioRepository.findById(theid)
                .orElseThrow(()-> new EntidadNoEncontradaException("El usuario con ID: "+ theid+ " no ha sido encontrado"));


        //Actualizamos el rol del usuario
        usuario.setActivo(estado);

        //Guardamos
        return usuarioResponseMapper.convertirAResponseDTO(usuario); // JPA actualizará automáticamente por @Transactional
    }

}
