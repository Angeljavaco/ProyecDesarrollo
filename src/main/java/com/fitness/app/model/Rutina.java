package com.fitness.app.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Rutina {

    private int id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @Min(value = 1, message = "La duración debe ser mayor a 0")
    private int duracion;

    public Rutina() {}

    public Rutina(int id, String nombre, String tipo, int duracion) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.duracion = duracion;
    }

    // ✅ GETTERS Y SETTERS COMPLETOS

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
}