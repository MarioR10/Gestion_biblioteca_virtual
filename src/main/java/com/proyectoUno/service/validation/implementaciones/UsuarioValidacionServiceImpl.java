package com.proyectoUno.service.validation.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadDuplicadaException;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.exception.ListaDeEntidadesVaciaException;
import com.proyectoUno.service.validation.interfaces.UsuarioValidacionService;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class UsuarioValidacionServiceImpl implements UsuarioValidacionService {
    @Override
    public void validarListaDeUsuariossNoVacia(List<Usuario> usuarios) {

        if (usuarios== null || usuarios.isEmpty()){
             throw new ListaDeEntidadesVaciaException("No se encontraron usuarios en la base de datos");
        }
    }

    @Override
    public Usuario validarUsuarioExistencia(Optional<Usuario> usuarioOptional) {

       Usuario usuario = usuarioOptional.orElseThrow(

                () ->  new EntidadNoEncontradaException( "El usuario buscado no ha sido encontrado"));

       return usuario;
    }

    @Override
    public void validarDatosActualizacion(Usuario datosActualizacion) {
        // Validar que al menos un campo viene para actualizar
        if (Stream.of(
                        datosActualizacion.getEmail(),
                        datosActualizacion.getRol())

                .allMatch(Objects::isNull)) {
            throw new ValidationException("Debe proporcionar al menos un campo para actualizar");
        }

    }

    @Override
    public void validarUsuarioNoDuplicado(Optional<Usuario> usuario, String email) {

        if (usuario.isPresent()){

            throw new EntidadDuplicadaException("Ya existe un usuario con email: "+ email);
        }
    }


    @Override
    public  void validarRolUsuario(String nuevoRol){

        if(!nuevoRol.equals("admin") && !nuevoRol.equals("usuario")){
            throw new RuntimeException("Rol no válido: " + nuevoRol);
        }
    }



}
