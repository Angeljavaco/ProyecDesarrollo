package com.fitness.app.clase.entity;

import jakarta.persistence.*;
import com.fitness.app.usuario.entity.Usuario;

import java.time.LocalDate;

@Entity
@Table(name = "clase")
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Usuario trainer;

    private int cupos;

    private boolean activo = true;

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoClase estado = EstadoClase.PROGRAMADA;

    public Clase() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getTrainer() {
        return trainer;
    }

    public void setTrainer(Usuario trainer) {
        this.trainer = trainer;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public EstadoClase getEstado() {
        return estado;
    }

    public void setEstado(EstadoClase estado) {
        this.estado = estado;
    }
}