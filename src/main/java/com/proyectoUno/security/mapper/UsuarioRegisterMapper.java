package com.proyectoUno.security.mapper;

import com.proyectoUno.entity.Usuario;
import com.proyectoUno.security.dto.RegisterRequest;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioRegisterMapper {


    @Mapping(target = "rol", constant = "User")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contrasena", qualifiedByName = "encode")

    /*
    @Context es una anotacion que nos permite decirle a MapStruct que lo que le vamos a pasar es una dependencia externa
    que se le inyectara. En este caso necesitamos  el bean PasswordEncoder.
    Una vez que se lo pasamos, sabe que cualquier metodo auxiliar que lo ocupe (marcado tambien con @Context) debe pasarselo
    porque es una dependencia que necesita para trabajar.
     */
    Usuario toEntity(RegisterRequest request, @Context PasswordEncoder passwordEncoder);


    /*
    Este metodo es util, el metodo princiipal lo llamada para aejecutar la logica de codificacion, este tambien necesita
    la dependencia, entonces el metodo prinicipal se la pasa. Practicamente lo que sucede internamente es: MapStruct va
    a mappear la contraseña pero ve anotaciones que le dicen "antes de mapear directamente el valor que trae request,
    aplicale el metodo que se especifica, pasale lo que necesita y luego mapea el resultado.
     */
    @Named("encode")
    default String encodePassword(String rawPassword, @Context PasswordEncoder passwordEncoder){

        return passwordEncoder.encode(rawPassword);
    }
}
