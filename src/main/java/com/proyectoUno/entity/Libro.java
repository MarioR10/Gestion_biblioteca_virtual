package com.proyectoUno.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table( name = "libro")
public class Libro {

    //Campos
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name= "UUID", strategy = "org.hibernate.id.UUIDGenerator" )
    @Column(name="id")
    private UUID id;


    @Column(name = "titulo")
    private String titulo;

    @Column(name = "autor")
    private String autor;

    @Column(name = "isbn")
    private  String isbn;

    @Column(name = "categoria")
    private  String categoria;

    @Column(name = "anio_de_publicacion")
    private Integer AnioDePublicacion;

    @Column(name = "cantidad_disponible")
    private Integer cantidadDisponible;

    @Column(name = "estado")
    private String estado;

    //Campos que ayudan a la relacion bidireccional con la entidad Prestamos
    @OneToMany(mappedBy = "libro")
    private List<Prestamo> prestamo= new ArrayList<>();

    //Constructores

    public Libro() {
    }

    public Libro(UUID id, String titulo, String isbn, String autor, String categoria, Integer anioDePublicacion, Integer cantidadDisponible, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.autor = autor;
        this.categoria = categoria;
        AnioDePublicacion = anioDePublicacion;
        this.cantidadDisponible = cantidadDisponible;
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

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
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
                ", cantidadDisponible=" + cantidadDisponible +
                ", estado='" + estado + '\'' +
                '}';
    }
}
