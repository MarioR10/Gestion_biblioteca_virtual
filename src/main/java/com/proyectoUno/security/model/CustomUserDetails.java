package com.proyectoUno.security.model;

/*
  Implementación concreta de UserDetails.
  Representa al usuario autenticado con sus permisos y estado.
  DEBE implementar la interfaz UserDetails.
  datos. Es como el "perfil" del usuario que Spring Security usa internamente.*/

import com.proyectoUno.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    private final UUID id;
    private final String email;
    private final String contrasena;
    private final String rol;
    private final boolean activo;

    //Inicializamos las variables desde la entidad Usuario
    public CustomUserDetails(Usuario usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.contrasena = usuario.getContrasena();
        this.rol = usuario.getRol();
        this.activo = usuario.getActivo();
    }

    //metodo que devuelve las autoridades, o roles en nuestro caso asinados al usuario
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //Logica para mapear el campo rol a una autoridad que comprenda spring security
        return Collections.singletonList( new SimpleGrantedAuthority(" ROLE_"+ rol.toUpperCase()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
