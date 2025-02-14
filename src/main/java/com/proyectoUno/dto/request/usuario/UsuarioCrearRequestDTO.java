package com.proyectoUno.dto.request.usuario;

import javax.validation.constraints.NotNull;

public class UsuarioCrearRequestDTO {


    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;
    @NotNull(message = "El apellido no puede ser nulo")
    private String apellido;
    @NotNull(message = "El email no puede ser nulo")
    private String email;
    @NotNull(message = "La contraseña no puede ser nulo")
    private String contraseña;
    @NotNull(message = "El rol no puede ser nulo")
    private String rol;

    //metodos setter and getter


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
