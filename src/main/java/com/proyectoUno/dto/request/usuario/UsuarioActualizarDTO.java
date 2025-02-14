package com.proyectoUno.dto.request.usuario;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class UsuarioActualizarDTO {

    private UUID id;

    @NotNull(message = "El email no puede ser nulo")
    private String email;

    @NotNull(message = "El rol no puede ser nulo")
    private String rol;

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

    public UUID getId() {
        return id;
    }
}
