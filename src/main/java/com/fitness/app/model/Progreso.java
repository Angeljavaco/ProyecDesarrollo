package com.fitness.app.model;

import jakarta.validation.constraints.Min;

public class Progreso {

    private int id;

    private int usuarioId;

    private int rutinaId;

    @Min(value = 0, message = "El progreso no puede ser negativo")
    private int porcentaje;

    public Progreso() {}

    public Progreso(int id, int usuarioId, int rutinaId, int porcentaje) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.rutinaId = rutinaId;
        this.porcentaje = porcentaje;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getRutinaId() { return rutinaId; }
    public void setRutinaId(int rutinaId) { this.rutinaId = rutinaId; }

    public int getPorcentaje() { return porcentaje; }
    public void setPorcentaje(int porcentaje) { this.porcentaje = porcentaje; }
}