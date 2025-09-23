package com.proyectoUno.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad que representa un Usuario en la base de datos.
 * Contiene información personal, credenciales, rol y estado de activación,
 * además de la relación bidireccional con la entidad Prestamo.
 */
@Entity
@Table( name = "usuario")
public class Usuario {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="id")
    private UUID id;   // Identificador único del usuario

    @Column(name ="nombre")
    private String nombre;   // Nombre del usuario

    @Column(name ="apellido")
    private String apellido; // Apellido del usuario


    @Column(name ="email")
    private String email;  // Correo electrónico del usuario

    @Column(name ="contrasena")
    private String contrasena;  // Contraseña del usuario


    @Column(name ="rol")
    private String rol;  // Rol del usuario en el sistema (por ejemplo, "Admin" o "User")


    /** Fecha de registro, generada automáticamente */
    @CreationTimestamp
    @Column(name= "fecha_registro", nullable = false,updatable = false)
    private LocalDateTime fechaRegistro;  //Fecha de registro, generada automáticamente


    @Column(name = "activo", insertable = false)
    private boolean activo; // Indica si el usuario está activo; valor por defecto en BD


    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Prestamo> prestamo= new ArrayList<>(); // Lista de préstamos asociados al usuario (relación bidireccional)

    // Constructores

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String email, String contrasena, String rol, LocalDateTime fechaRegistro, UUID id, boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
        this.id = id;
        this.activo=activo;
    }


    //Metodos getters and setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<Prestamo> getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(List<Prestamo> prestamo) {
        this.prestamo = prestamo;
    }


    // ==========================
    // Métodos de conveniencia
    // ==========================

    /**
     * Agrega un préstamo a la lista de préstamos del usuario.
     * @param tempPrestamo objeto Prestamo a agregar
     */
    public void agregarPrestamo( Prestamo tempPrestamo){
        prestamo.add(tempPrestamo);
    }

    //toString
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", rol='" + rol + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", activo=" + activo +
                ", prestamo=" + prestamo +
                '}';
    }
}
