package com.proyectoUno.security.entity;


import com.proyectoUno.entity.Usuario;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id")
    private UUID id;

    @Column(name= "token")
    private String token;

    @CreationTimestamp
    @Column(name= "fecha_expiracion", nullable = false,updatable = false)
    private Instant fechaExpiracion;

    @Column(name = "revoked")
    private  Boolean revoked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    public RefreshToken() {
    }

    public RefreshToken(String token, Instant fechaExpiracion, Boolean revoked, Usuario usuario) {
        this.token = token;
        this.fechaExpiracion = fechaExpiracion;
        this.revoked = revoked;
        this.usuario = usuario;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Instant fechaEspiracion) {
        this.fechaExpiracion = fechaEspiracion;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
