package com.fitness.app.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Ejercicio {

    private int id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El grupo muscular es obligatorio")
    private String grupoMuscular;

    @Min(value = 1, message = "Las repeticiones deben ser mayor a 0")
    private int repeticiones;

    public Ejercicio() {}

    public Ejercicio(int id, String nombre, String grupoMuscular, int repeticiones) {
        this.id = id;
        this.nombre = nombre;
        this.grupoMuscular = grupoMuscular;
        this.repeticiones = repeticiones;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getGrupoMuscular() { return grupoMuscular; }
    public void setGrupoMuscular(String grupoMuscular) { this.grupoMuscular = grupoMuscular; }

    public int getRepeticiones() { return repeticiones; }
    public void setRepeticiones(int repeticiones) { this.repeticiones = repeticiones; }
}