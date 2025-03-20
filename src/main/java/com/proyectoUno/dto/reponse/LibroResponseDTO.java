package com.proyectoUno.dto.reponse;

import java.util.UUID;

public class LibroResponseDTO {

    //Datos necesario a enviar

    private UUID id;
    private String titulo;
    private String autor;
    private String isbn;
    private String categoria;
    private Integer anioDePublicacion;
    private String estado;



    //Getter and Setter


    public UUID getId() {return id;}

    public void setId(UUID id) {this.id = id;}

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

    public String getIsbn() {return isbn; }

    public void setIsbn(String isbn) {this.isbn = isbn; }

    public String getCategoria() { return categoria;}

    public void setCategoria(String categoria) {this.categoria = categoria;}

    public Integer getAnioDePublicacion() {return anioDePublicacion; }

    public void setAnioDePublicacion(Integer anioDePublicacion) {this.anioDePublicacion = anioDePublicacion;}

    public String getEstado() {return estado;}

    public void setEstado(String estado) {
        this.estado = estado;
    }
}


