package com.proyectoUno.service.Internal.implementaciones;

import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.repository.UsuarioRepository;
import com.proyectoUno.service.Internal.interfaces.UsuarioServiceInternal;
import com.proyectoUno.service.validation.interfaces.UsuarioValidacionService;
import com.proyectoUno.service.validation.interfaces.ValidacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceInternalImpl implements UsuarioServiceInternal {


    private final ValidacionService validacionService;
    private UsuarioRepository usuarioRepository;
    private UsuarioValidacionService usuarioValidacionService;

    @Autowired
    public UsuarioServiceInternalImpl(UsuarioRepository usuarioRepository, UsuarioValidacionService usuarioValidacionService, ValidacionService validacionService){

        this.usuarioRepository=usuarioRepository;
        this.usuarioValidacionService=usuarioValidacionService;
        this.validacionService = validacionService;
    }

    //Metodos actuales

    @Override
    public Usuario encontrarUsuarioPorId(UUID id){
        //Encontramos el optional
        Optional <Usuario> usuarioOptional= usuarioRepository.findById(id);
        //Validamos el usuario
        Usuario usuario= validacionService.validarExistencia(usuarioOptional, "usuario");
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
    public void crearUsuario(List<Usuario> usuarios){
       //Obtenemos la lista de emails nuevos
        List<String> emailsNuevos = usuarios.stream()
                .map(Usuario::getEmail)
                        .collect(Collectors.toList());

        // Validamos que no estén duplicados los emails dentro de la lista
        usuarioValidacionService.validarDuplicadosListaEntrada(emailsNuevos);
        // Validamos que los emails no existan en la base de datos
        List<Usuario> usuariosExistentes = usuarioRepository.findByEmailIn(emailsNuevos);
        usuarioValidacionService.validarDuplicadosBaseDeDatos(usuariosExistentes);
        // Guardamos los nuevos usuarios verificados
        usuarioRepository.saveAll(usuarios);
    }

    @Override
    public Usuario encontrarUsuarioPorEmail(String email){
        //Encontramos el usuario por email
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(email);
        //Verificamos que este presente en la abse de datos
        Usuario usuario=  validacionService.validarExistencia(usuarioEncontrado, "usuario");
        return usuario;
    }

    //metodos paginados
    @Override
    public Page<Usuario> encontrarUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        validacionService.validarPaginaNoVacia(usuarios, "Usuario");
        return usuarios;
    }



}
