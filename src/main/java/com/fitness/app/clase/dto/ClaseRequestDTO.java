package com.fitness.app.clase.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ClaseRequestDTO {

    @NotBlank(message = "El nombre de la clase es obligatorio.")
    private String nombre;

    @NotBlank(message = "La descripción de la clase es obligatoria.")
    private String descripcion;

    @Min(value = 1, message = "La clase debe tener al menos un cupo.")
    private int cupos;

    @NotNull(message = "La fecha de la clase es obligatoria.")
    @Future(message = "La clase no puede programarse en una fecha pasada.")
    private LocalDate fecha;

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCupos() {
        return cupos;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

}
