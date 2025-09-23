package com.proyectoUno.security.mapper;

import com.proyectoUno.entity.Usuario;
import com.proyectoUno.security.dto.RegisterRequest;
import org.mapstruct.*;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Mapper para convertir un RegisterRequest en una entidad Usuario.
 * Utiliza MapStruct para mapear campos automáticamente y aplicar lógica adicional como codificación de contraseña.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioRegisterMapper {

    /**
     * Convierte un DTO RegisterRequest en una entidad Usuario.
     * - Asigna el rol por defecto "User".
     * - Ignora el ID (se generará automáticamente en la base de datos).
     * - Codifica la contraseña usando PasswordEncoder antes de asignarla.
     * @param request DTO con datos del usuario
     * @param passwordEncoder Bean inyectado para codificar contraseñas
     * @return Usuario mapeado y listo para persistir
     */
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


    /**
     * Método auxiliar para codificar la contraseña.
     * MapStruct lo invoca automáticamente al mapear la propiedad 'contrasena'.
     *     Este método auxiliar se utiliza para codificar la contraseña antes de mapearla en la entidad Usuario.
     *     MapStruct lo llama automáticamente al mapear la propiedad 'contrasena'. La anotación @Context permite
     *     pasar la dependencia PasswordEncoder necesaria para la codificación. En otras palabras, MapStruct aplica
     *     este método antes de asignar el valor final al campo de la entidad.
     * @param rawPassword Contraseña en texto plano
     * @param passwordEncoder Bean inyectado para codificar la contraseña
     * @return Contraseña codificada
     */

    @Named("encode")
    default String encodePassword(String rawPassword, @Context PasswordEncoder passwordEncoder){

        return passwordEncoder.encode(rawPassword);
    }
}
