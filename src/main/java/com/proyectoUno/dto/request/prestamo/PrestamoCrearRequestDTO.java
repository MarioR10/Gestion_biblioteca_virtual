package com.proyectoUno.dto.request.prestamo;

import java.time.LocalDateTime;
import java.util.UUID;

public class PrestamoCrearRequestDTO {

    private UUID idUsuario;
    private UUID idLibro;

    //Getter and Setter


    public UUID getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(UUID idUsuario) {
        this.idUsuario = idUsuario;
    }

    public UUID getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(UUID idLibro) {
        this.idLibro = idLibro;
    }
}
