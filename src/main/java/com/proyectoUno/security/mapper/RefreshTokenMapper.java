package com.proyectoUno.security.mapper;

import com.proyectoUno.entity.Usuario;
import com.proyectoUno.security.entity.RefreshToken;
import com.proyectoUno.security.jwt.JwtService;
import org.mapstruct.*;

import java.time.Instant;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RefreshTokenMapper {

    @Mapping(target = "token", source = "refreshToken")
    @Mapping(target = "revoked", constant= "false")
    @Mapping(target = "usuario", source = "usuario")
    @Mapping(target = "fechaExpiracion", qualifiedByName = "ObtenerFecha")
    RefreshToken toEntity(String refreshToken, Usuario usuario, @Context JwtService jwtService);

    @Named("ObtenerFecha")
    default Instant fechaExpiracion ( @Context JwtService jwtService){
        return Instant.now().plusMillis(jwtService.getEXPIRATION_REFRESH_TOKEN());
    }
}
