package com.proyectoUno.dto.reponse;

import java.time.LocalDateTime;

public class PrestamoResponseDTO {

    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;
    private String estado;


    //Getter and Setter


    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
