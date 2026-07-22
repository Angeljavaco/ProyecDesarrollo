package com.fitness.app.rolusuario.dto;

import com.fitness.app.rolusuario.entity.RolUsuario;

public class RolUsuarioResponseDTO {

    private int id;
    private int usuarioId;
    private String usuarioNombre;
    private int rolId;
    private String rolNombre;
    private boolean activo;

    public RolUsuarioResponseDTO(RolUsuario rolUsuario) {
        this.id = rolUsuario.getId();
        this.usuarioId = rolUsuario.getUsuario().getId();
        this.usuarioNombre = rolUsuario.getUsuario().getNombre();
        this.rolId = rolUsuario.getRol().getId();
        this.rolNombre = rolUsuario.getRol().getNombre();
        this.activo = rolUsuario.isActivo();
    }

    public RolUsuarioResponseDTO() {
    }

    public int getId() {
        return id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public int getRolId() {
        return rolId;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}