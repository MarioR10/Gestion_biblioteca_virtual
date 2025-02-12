package com.proyectoUno.dto.request.libro;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class LibroActualizarRequestDTO {

    private UUID id;
    @NotNull(message = "El título no puede ser nulo")
    private String titulo;
    @NotNull(message = "El autor no puede ser nulo")
    private String autor;
    @NotNull(message = "La categoría no puede ser nula")
    private String categoria;

    @NotNull(message = "El estado no puede ser nulo")
    private  String estado;


    //Getter and Setter


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public UUID getId() {
        return id;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
