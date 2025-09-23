package com.proyectoUno.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad que representa un Libro en la base de datos.
 * Contiene información básica del libro y mantiene la relación bidireccional con Prestamo.
 */
@Entity
@Table( name = "libro")
public class Libro {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name= "UUID", strategy = "org.hibernate.id.UUIDGenerator" )
    @Column(name="id")
    private UUID id; //Identificador único del libro

    @Column(name = "titulo")
    private String titulo; //Título del libro

    @Column(name = "autor")
    private String autor; // Autor del libro

    @Column(name = "isbn")
    private  String isbn; // Código ISBN del libro

    @Column(name = "categoria")
    private  String categoria; // Categoría o género del libro

    @Column(name = "anio_de_publicacion")
    private Integer AnioDePublicacion; // Año de publicación

    @Column(name = "estado", insertable = false)
    private String estado; // Estado del libro en la base de datos (por ejemplo, "activo" o "prestado")

    @OneToMany(mappedBy = "libro")
    private List<Prestamo> prestamo= new ArrayList<>();  // Lista de préstamos asociados al libro (relación bidireccional)

    //Constructores

    public Libro() {
    }

    public Libro(UUID id, String titulo, String isbn, String autor, String categoria, Integer anioDePublicacion, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.autor = autor;
        this.categoria = categoria;
        this.AnioDePublicacion = anioDePublicacion;
        this.estado = estado;
    }

    //getters and setter

   public UUID getId(){
       return id;
   }

   public void setId(UUID id){

       this.id=id;
   }

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
        return AnioDePublicacion;
    }

    public void setAnioDePublicacion(Integer anioDePublicacion) {
        AnioDePublicacion = anioDePublicacion;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Prestamo> getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(List<Prestamo> prestamo) {
        this.prestamo = prestamo;
    }

    /**
     * Agrega un préstamo a la lista de préstamos del libro.
     * @param tempPrestamo objeto Prestamo a agregar
     */
    public void agregarPrestamo( Prestamo tempPrestamo){
        prestamo.add(tempPrestamo);
    }

    //To String
    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", isbn='" + isbn + '\'' +
                ", categoria='" + categoria + '\'' +
                ", AnioDePublicacion=" + AnioDePublicacion +
                ", estado='" + estado + '\'' +
                '}';
    }
}