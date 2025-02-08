package com.proyectoUno.dto.reponse;

public class LibroResponseDTO {

    //Datos necesario a enviar al ususario


    private String titulo;
    private String autor;
    private String estado;



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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}


