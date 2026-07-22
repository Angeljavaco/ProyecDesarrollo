package com.fitness.app.clase.dto;

import com.fitness.app.clase.entity.Clase;
import com.fitness.app.clase.entity.EstadoClase;

import java.time.LocalDate;

public class ClaseResponseDTO {

    private int id;
    private String nombre;
    private String descripcion;
    private int cupos;
    private boolean activo;
    private LocalDate fecha;
    private EstadoClase estado;

    private int trainerId;
    private String trainerNombre;

    public ClaseResponseDTO(Clase clase) {
        this.id = clase.getId();
        this.nombre = clase.getNombre();
        this.descripcion = clase.getDescripcion();
        this.cupos = clase.getCupos();
        this.activo = clase.isActivo();
        this.fecha = clase.getFecha();
        this.estado = clase.getEstado();

        this.trainerId = clase.getTrainer().getId();
        this.trainerNombre = clase.getTrainer().getNombre();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCupos() {
        return cupos;
    }

    public boolean isActivo() {
        return activo;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public String getTrainerNombre() {
        return trainerNombre;
    }

    public EstadoClase getEstado() {
        return estado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public void setTrainerNombre(String trainerNombre) {
        this.trainerNombre = trainerNombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

}
