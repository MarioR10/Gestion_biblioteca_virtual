package com.proyectoUno.dto.reponse;

import java.time.LocalDateTime;
import java.util.UUID;

public class PrestamoResponseDTO {

    private UUID id;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;
    private String estado;

    //DTOs anidados al DTO de prestamo (para enriquecer la informacion mostrada al mostrar el prestamo)
    private UsuarioResponseDTO usuarioAsociado; // Anidado
    private LibroResponseDTO libroAsociado; // Anidado
    //Getter and Setter
    public LocalDateTime getFechaPrestamo() {return fechaPrestamo;}
    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {this.fechaPrestamo = fechaPrestamo;}
    public LocalDateTime getFechaDevolucion() {return fechaDevolucion;}
    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {this.fechaDevolucion = fechaDevolucion;}

    public String getEstado() {return estado;}

    public void setEstado(String estado) {this.estado = estado;}

    public UUID getId() {return id;}

    public void setId(UUID id) {this.id = id;}
    public UsuarioResponseDTO getUsuarioAsociado() {return usuarioAsociado;}
    public void setUsuarioAsociado(UsuarioResponseDTO usuarioAsociado) {this.usuarioAsociado = usuarioAsociado;}
    public LibroResponseDTO getLibroAsociado() {return libroAsociado;}
    public void setLibroAsociado(LibroResponseDTO libroAsociado) {this.libroAsociado = libroAsociado;}
}
