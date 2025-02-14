package com.proyectoUno.dto.request.libro;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class LibroActualizarRequestDTO {


    @NotNull(message = "El título no puede ser nulo")
    private String titulo;
    @NotNull(message = "El autor no puede ser nulo")
    private String autor;
    @NotNull(message = "La categoría no puede ser nula")
    private String categoria;



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


}
