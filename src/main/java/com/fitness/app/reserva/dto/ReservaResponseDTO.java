package com.fitness.app.reserva.dto;

import com.fitness.app.reserva.entity.Reserva;

public class ReservaResponseDTO {

    private int id;
    private boolean activo;

    private int usuarioId;
    private String usuarioNombre;

    private int claseId;
    private String claseNombre;

    public ReservaResponseDTO(Reserva reserva) {
        this.id = reserva.getId();
        this.activo = reserva.isActivo();

        this.usuarioId = reserva.getUsuario().getId();
        this.usuarioNombre = reserva.getUsuario().getNombre();

        this.claseId = reserva.getClase().getId();
        this.claseNombre = reserva.getClase().getNombre();
    }

    public int getId() {
        return id;
    }

    public boolean isActivo() {
        return activo;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public int getClaseId() {
        return claseId;
    }

    public String getClaseNombre() {
        return claseNombre;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public void setClaseId(int claseId) {
        this.claseId = claseId;
    }

    public void setClaseNombre(String claseNombre) {
        this.claseNombre = claseNombre;
    }

}
