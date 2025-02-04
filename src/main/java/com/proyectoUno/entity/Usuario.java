package com.proyectoUno.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table( name = "usuario")
public class Usuario {

    //Campos
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="id")
    private UUID id;

    @Column(name ="nombre")
    private String name;

    @Column(name ="apellido")
    private String apellido;

    @Column(name ="email")
    private String email;

    @Column(name ="contrasena")
    private String contrasena;

    @Column(name ="rol")
    private String rol;
    @CreationTimestamp
    @Column(name= "fecha_registro", nullable = false,updatable = false)
    private LocalDateTime fechaRegistro;

    //Campos que ayudan a la relacion bidireccional con la entidad Prestamos
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Prestamo> prestamo= new ArrayList<>();

    // Constructores

    public Usuario() {
    }

    public Usuario(String name, String apellido, String email, String contrasena, String rol, LocalDateTime fechaRegistro, UUID id) {
        this.name = name;
        this.apellido = apellido;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
        this.id = id;
    }


    //Metodos getters and setters


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    //Metodos de conveniencia

    public void agregarPrestamo( Prestamo tempPrestamo){

        prestamo.add(tempPrestamo);

    }

    //toString

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", rol='" + rol + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
