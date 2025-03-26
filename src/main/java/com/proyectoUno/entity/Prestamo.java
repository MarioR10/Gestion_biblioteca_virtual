package com.proyectoUno.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "prestamo")
public class Prestamo {

    //Campos
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column( name = "id")
    private UUID id;

    @CreationTimestamp
    @Column(name = "fecha_prestamo")
    private LocalDateTime fechaPrestamo;


    @Column(name = "fecha_devolucion", insertable = false)
    private LocalDateTime fechaDevolucion;

    @Column( name="estado",insertable = false)
    private String estado;


    //Campos que ayudan a las relaciones

    @ManyToOne
    @JoinColumn(name= "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn( name= "id_libro", referencedColumnName = "id")
    private Libro libro;

    //Constructores


    public Prestamo() {
    }

    public Prestamo(LocalDateTime fechaPrestamo, UUID id, LocalDateTime fechaDevolucion, String estado, Usuario usuario, Libro libro) {
        this.fechaPrestamo = fechaPrestamo;
        this.id = id;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
        this.usuario = usuario;
        this.libro = libro;
    }

    //Getter and Setter

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }


    //toString

    @Override
    public String toString() {
        return "Prestamo{" +
                "id=" + id +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucion=" + fechaDevolucion +
                ", estado='" + estado + '\'' +
                ", usuario=" + usuario +
                ", libro=" + libro +
                '}';
    }
}

