package com.proyectoUno.dto.request.usuario;

import jakarta.persistence.Column;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class UsuarioActualizarDTO {

    @NotNull(message = "El nombre no puede ser nulo")
    private String nombre;

    @NotNull(message = "El apellido no puede ser nulo")
    private String apellido;

    @NotNull(message = "El email no puede ser nulo")
    private String email;

    @NotNull(message = "El rol no puede ser nulo")
    private String rol;
    @NotNull(message = "El estado dea ctividad no puede ser nulo")
    private boolean activo;

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getApellido() {return apellido;}

    public void setApellido(String apellido) {this.apellido = apellido;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean getActivo() {return activo;}

    public void setActivo(boolean activo) {this.activo = activo;}
}
