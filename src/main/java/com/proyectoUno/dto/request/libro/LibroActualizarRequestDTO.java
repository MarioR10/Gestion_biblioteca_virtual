package com.proyectoUno.dto.request.libro;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public class LibroActualizarRequestDTO {


    @NotNull(message = "El título no puede ser nulo")
    @Size(min = 1, max = 100, message = "El título debe tener entre 1 y 100 caracteres")
    private String titulo;
    @NotNull(message = "El autor no puede ser nulo")
    @Size(min = 1, max = 50, message = "El autor debe tener entre 1 y 50 caracteres")
    private String autor;
    @NotNull(message = "La categoría no puede ser nula")
    @Size(min = 1, max = 50, message = "La categoría debe tener entre 1 y 50 caracteres")
    private String categoria;

    @NotNull(message = "El año de publicación no puede ser nulo")
    @Min(value = 1, message = "El año de publicación debe ser mayor a 0")
    private Integer anioDePublicacion;


    //Getter and Setter

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getCategoria() {
        return categoria;
    }
    public Integer getAnioDePublicacion() {return anioDePublicacion;
    }
    public void setAnioDePublicacion(Integer anioDePublicacion) {this.anioDePublicacion = anioDePublicacion;
    }
}
