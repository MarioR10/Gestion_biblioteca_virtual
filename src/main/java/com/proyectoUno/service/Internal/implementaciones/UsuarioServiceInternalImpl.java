package com.proyectoUno.service.Internal.implementaciones;

import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadDuplicadaException;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.exception.SolicitudActualizacionInvalidaException;
import com.proyectoUno.exception.SolicitudConDuplicadosException;
import com.proyectoUno.repository.UsuarioRepository;
import com.proyectoUno.service.Internal.interfaces.UsuarioServiceInternal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UsuarioServiceInternalImpl implements UsuarioServiceInternal {


    private UsuarioRepository usuarioRepository;


    @Autowired
    public UsuarioServiceInternalImpl(UsuarioRepository usuarioRepository){
        this.usuarioRepository=usuarioRepository;
    }

    //Metodos actuales

    @Override
    public Usuario encontrarUsuarioPorId(UUID id){
        //Validamos el usuario
        Usuario usuario= usuarioRepository.findById(id)
                .orElseThrow(()-> new EntidadNoEncontradaException("Usuario","ID",id));

        return usuario;
    }

    @Override
    @Transactional
    public void eliminarUsuarioPorId(UUID id){

        //Encontramos el usuario
         Usuario usuario= encontrarUsuarioPorId(id);
        //Eliminamos el usuario encontrado
            usuarioRepository.delete(usuario);
    }


    @Override
    @Transactional
    public Usuario actualizarUsuario(UUID id, Usuario datosValidar) {

        // Validar que al menos un campo viene para actualizar
        if (Stream.of(
                        datosValidar.getNombre(),
                        datosValidar.getApellido(),
                        datosValidar.getEmail(),
                        datosValidar.getRol(),
                        datosValidar.getActivo())

                .allMatch(Objects::isNull)) {
            throw new SolicitudActualizacionInvalidaException("Usuario");
        }

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


       /*De todos los datos de los usuarios que vienen en la lista solo interesa el email para verificar duplicados,
       por lo que transformamos la lista usuarios  de tipo Usuario en una lista String que contenga solo los emails*/
        List<String> emailsNuevos =
                //stream() crea un flujo de datos de los elementos de una lista para que puedan ser procesados uno a uno
                usuarios.stream()
                //map() transforma cada elemento del stream; aquí, convierte cada objeto Usuario en su email usando el metodo getEmail().
                .map(Usuario::getEmail)
                //collect() recolecta los emails (Strings) en una lista; es necesario porque los streams son temporales y los resultados se pierden si no se almacenan.
                        .collect(Collectors.toList());

        // FASE 1: VALIDACIÓN INTERNA DE LA SOLICITUD (verificamos que en la lista no vengan emails duplicados

        //1. Necesitamos un set (no permite elementos duplciados).
        Set<String> emailsUnicos = new HashSet<>();

        //2. Filtramos la lista , para encontrar los duplicados
        List<String> emailsDuplicados =

                //stream() crea un flujo de datos de los elementos de una lista para que puedan ser procesados uno a uno
                emailsNuevos.stream()

                /*cada email se intenta añadir a la lista, el metodo !emailsUnicos.add(email)), regresa un booleano
                 TRUE -> si se agrego el elemento ; FALSE -> No se agrego el elemento. En la sentencia estamos negando ese resultado, de manera que cuando
                 haya duplicado (FALSE) pase a ser TRUE.
                 Se hace esto, debido a que el metodo filter() solo toma los elementos que devuelven TRUE, (en este caso serian solo los duplicados)
                 */
                .filter(email -> !emailsUnicos.add(email))
                        .collect(Collectors.toList());

        if( !emailsDuplicados.isEmpty()){
             throw new SolicitudConDuplicadosException("email", emailsDuplicados);
        }

        // FASE 2: VALIDACIÓN CONTRA LA BASE DE DATOS (verificamos que la lista ya verificada no tenga duplicados con datos ya existentes en la DB)

        // 1. buscamos en la base de datos usuarios con los correos nuevos
        List<Usuario> usuariosExistentes = usuarioRepository.findByEmailIn(emailsNuevos);

        //2. verificamos si la lista no esta vacia (significa que si existen duplicados)
        if(!usuariosExistentes.isEmpty()){

            //Obtenemos los emails que ya existen
            List<String> emailsExistentes =
                    //stream() crea un flujo de datos de los elementos de una lista para que puedan ser procesados uno a uno
                    usuariosExistentes.stream()

                            //map() transforma cada elemento del stream; aquí, convierte cada objeto Usuario en su email usando el metodo getEmail().
                            .map(Usuario::getEmail)
                            //collect() recolecta los emails (Strings) en una lista; es necesario porque los streams son temporales y los resultados se pierden si no se almacenan.
                            .collect(Collectors.toList());
            //lanzamos excepcion
            throw new EntidadDuplicadaException("Usuario","email",emailsExistentes);
        }

        // FASE 3: PERSISTENCIA

        // Guardamos los nuevos usuarios verificados
        usuarioRepository.saveAll(usuarios);
    }

    @Override
    public Usuario encontrarUsuarioPorEmail(String email){
        //Encontramos el usuario por email
        Usuario usuarioEncontrado = usuarioRepository.findByEmail(email)
                .orElseThrow(()->  new EntidadNoEncontradaException("Usuario"));

        return usuarioEncontrado;
    }

    //metodos paginados
    @Override
    public Page<Usuario> encontrarUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        return usuarios;
    }



}
