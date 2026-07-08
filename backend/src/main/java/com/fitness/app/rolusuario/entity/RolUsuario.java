package com.fitness.app.rolusuario.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fitness.app.rol.entity.Rol;
import com.fitness.app.usuario.entity.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "rol_usuario")
public class RolUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "fk_rol_usuario_usuario"))
    private Usuario usuario;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_rol", nullable = false, foreignKey = @ForeignKey(name = "fk_rol_usuario_rol"))
    private Rol rol;

    public RolUsuario() {
    }

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

    public int getIdRol() {
        return rol != null ? rol.getId() : 0;
    }

    public void setIdRol(int idRol) {
        Rol rol = new Rol();
        rol.setId(idRol);
        this.rol = rol;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
