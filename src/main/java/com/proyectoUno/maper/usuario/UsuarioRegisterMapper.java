package com.proyectoUno.maper.usuario;

import com.proyectoUno.entity.Usuario;
import com.proyectoUno.security.dto.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Context;
import org.springframework.security.crypto.password.PasswordEncoder;



@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioRegisterMapper {

    //Campo objetivo "rol", le asiganamos User por defecto al mappeo
    @Mapping(target = "rol", constant = "User")

    //Campo objetivo "id", ignoramos en el mappeo
    @Mapping(target = "id", ignore = true)

    //Campo objetivo "contrasena", le aplicamos un metodo para codificar la contraseña antes de mappearla, llamamos al metodo con java()
    @Mapping( target = "contrasena", expression = "java(encodePassword(request.contrasena()), passwordEncored))")
    Usuario toEntity(RegisterRequest request, @Context PasswordEncoder passwordEncoder);

    //Metodo default al que estamos llamando en java(), realiza la logica de codificacion, @Context ayuda a que MapStruct sepa que PasswordEncoder
    // es una dependencia que se le inyectara desde afuera
    default  String encodePassword(String rawPassword, @Context PasswordEncoder passwordEncoder){
        return passwordEncoder.encode(rawPassword);
    }
}
