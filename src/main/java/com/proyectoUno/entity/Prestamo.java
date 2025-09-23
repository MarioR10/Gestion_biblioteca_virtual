package com.proyectoUno.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa un Préstamo en la base de datos.
 * Contiene información sobre la fecha del préstamo, devolución, estado
 * y relaciones con Usuario y Libro.
 */
@Entity
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column( name = "id")
    private UUID id; // Identificador único del préstamo

    @CreationTimestamp
    @Column(name = "fecha_prestamo")
    private LocalDateTime fechaPrestamo; // Fecha y hora en que se realizó el préstamo

    @Column(name = "fecha_devolucion")
    private LocalDateTime fechaDevolucion; // Fecha y hora de devolución del libro (puede ser null si aún no se ha devuelto)

    @Column( name="estado",insertable = false)
    private String estado;  // Estado del préstamo (por ejemplo, "activo", "devuelto" o "vencido")

    // ==========================
    // Relaciones
    // ==========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "id_usuario", referencedColumnName = "id")
    private Usuario usuario;  // Usuario que realizó el préstamo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name= "id_libro", referencedColumnName = "id")
    private Libro libro; // Libro que se presta

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

