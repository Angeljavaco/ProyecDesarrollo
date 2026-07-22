package com.fitness.app.reporte.dto;

public class ReporteInscritoDTO {

    private int usuarioId;
    private String nombre;
    private String email;
    private String telefono;

    public ReporteInscritoDTO() {
    }

    public ReporteInscritoDTO(
            int usuarioId,
            String nombre,
            String email,
            String telefono
    ) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}