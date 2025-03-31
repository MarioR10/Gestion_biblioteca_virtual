package com.proyectoUno.service.validation.implementaciones;

import com.proyectoUno.entity.Libro;
import com.proyectoUno.entity.Usuario;
import com.proyectoUno.exception.EntidadDuplicadaException;
import com.proyectoUno.exception.EntidadNoEncontradaException;
import com.proyectoUno.exception.ListaDeEntidadesVaciaException;
import com.proyectoUno.service.validation.interfaces.UsuarioValidacionService;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;
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
                        datosActualizacion.getNombre(),
                        datosActualizacion.getApellido(),
                        datosActualizacion.getEmail(),
                        datosActualizacion.getRol(),
                        datosActualizacion.getActivo())

                .allMatch(Objects::isNull)) {
            throw new ValidationException("Debe proporcionar al menos un campo para actualizar");
        }

    }

    @Override
    public void validarDuplicadosListaEntrada(List<String> emailsNuevos) {

        // Convertimos la lista en un Set para eliminar duplicados automáticamente
        Set<String> emailsUnicos = new HashSet<>(emailsNuevos);

        // Si el tamaño del Set es menor que el de la lista original, significa que había duplicados
        if (emailsUnicos.size() < emailsNuevos.size())
            throw new IllegalArgumentException("La lista contiene emails duplicados.");
    }

    @Override
    public void validarDuplicadosBaseDeDatos(List<Usuario> usuariosExistentes) {

        /* Usar un Set asegura que, incluso en un caso hipotético donde usuariosExistentes tenga duplicados
         (por un error externo, quiten UNIQUE DE LA DB), no reportemos el mismo email varias veces en el mensaje de error. */

        if( !usuariosExistentes.isEmpty()){

            Set<String> emailsDuplicados = usuariosExistentes.stream()
                    .map(Usuario::getEmail)
                    .collect(Collectors.toSet());

            throw new IllegalArgumentException("Los siguientes emails ya están registrados: " + emailsDuplicados);
        }


    }


}
