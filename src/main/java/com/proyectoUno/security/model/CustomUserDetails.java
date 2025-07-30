package com.proyectoUno.security.model;

import com.proyectoUno.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Esta clase adapta la entidad de dominio `Usuario` para proporcionar los datos mínimos que Spring Security necesita
 * para los procesos de autenticación y autorización.Spring utiliza esta clase para obtener esos datos.
 */
public class CustomUserDetails implements UserDetails {


    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario){
        this.usuario=usuario;
    }

    /*
     *Informacion necesaria/requerida por Spring Security
     * 1.Role/roles
     * 2.Username/(puede ser mediante email tambien)
     * 3.Contraseñra
     * otros
     */

    /**
     * Metodo importante para la autorización basada en roles.
     * Spring Security lo utiliza para obtener las autoridades (roles y/o permisos) que tiene el usuario.
     * @return Una colección genérica que puede contener cualquier tipo de objeto (?) que sea GrantedAuthority
     * (o cualquier subclase que la implemente, como SimpleGrantedAuthority).
     * Esta colección representará los roles del usuario.
     * Comúnmente, los objetos dentro de esta colección serán instancias de SimpleGrantedAuthority,
     * cada una conteniendo la cadena del rol (ej., "ROLE_ADMIN").
     */
    @Override
    public Collection <? extends GrantedAuthority> getAuthorities(){

        return
                //Crea una lista inmutable que contiene un solo elemento.
                // Util cuando sabemos que el usuario solo tendra un ROL
                Collections.singletonList(
                        //Mapea el Rol de la entidad usuario a una autoridad de Spring Security. Crea el Objeto el cual almacena el ROL
                        //El prefijo ROLE es una convencion para que spring reconozca el rol
                        new SimpleGrantedAuthority("ROLE_"+usuario.getRol()));
}
    @Override
    public  String getUsername(){

        return usuario.getEmail();
    }

    @Override
    public String getPassword(){
        return usuario.getContrasena();
    }

    //ESTADO DE LA CUENTA
    @Override
    public boolean isEnabled(){
        return usuario.getActivo();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    //otros que vienen por default en la interfaz
}
