package com.fitness.app.reserva.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fitness.app.clase.entity.Clase;
import com.fitness.app.usuario.entity.Usuario;
import jakarta.persistence.*;

@Entity
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "fk_reserva_usuario"))
    private Usuario usuario;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_clase", nullable = false, foreignKey = @ForeignKey(name = "fk_reserva_clase"))
    private Clase clase;

    private String fechaReserva;

    private String estado;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return usuario != null ? usuario.getId() : 0;
    }

    public void setIdUsuario(int idUsuario) {
        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        this.usuario = usuario;
    }

    public int getIdClase() {
        return clase != null ? clase.getId() : 0;
    }

    public void setIdClase(int idClase) {
        Clase clase = new Clase();
        clase.setId(idClase);
        this.clase = clase;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public String getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
