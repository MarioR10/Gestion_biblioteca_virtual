package com.proyectoUno.dto.request.libro;

import javax.validation.constraints.NotNull;

public class LibroCrearRequestDTO {


    @NotNull(message = "El título no puede ser nulo")
    private String titulo;
    @NotNull(message = "El autor no puede ser nulo")
    private String autor;
    @NotNull(message = "La categoría no puede ser nula")
    private String categoria;

    @NotNull(message = "El estado no puede ser nulo")
    private  String estado;


    @NotNull(message = "El isbn no puede ser nulo")
    private String isbn;

    @NotNull(message = "El año de publicacion no puede ser nulo")
    private Integer anioDePublicacion;

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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getAnioDePublicacion() {
        return anioDePublicacion;
    }

    public void setAnioDePublicacion(Integer anioDePublicacion) {
        this.anioDePublicacion = anioDePublicacion;
    }
}
