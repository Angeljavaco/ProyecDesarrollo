package com.fitness.app.rolusuario.service;

import com.fitness.app.rol.entity.Rol;
import com.fitness.app.rolusuario.entity.RolUsuario;
import com.fitness.app.rolusuario.repository.RolUsuarioRepository;
import com.fitness.app.usuario.entity.Usuario;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolUsuarioService {

    private final RolUsuarioRepository rolUsuarioRepository;
    private final EntityManager entityManager;

    public RolUsuarioService(RolUsuarioRepository rolUsuarioRepository,
                             EntityManager entityManager) {
        this.rolUsuarioRepository = rolUsuarioRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public RolUsuario asignarRol(RolUsuario rolUsuario) {
        int idUsuario = rolUsuario.getIdUsuario();
        int idRol = rolUsuario.getIdRol();

        if (idUsuario > 0) {
            rolUsuario.setUsuario(entityManager.getReference(Usuario.class, idUsuario));
        }

        if (idRol > 0) {
            rolUsuario.setRol(entityManager.getReference(Rol.class, idRol));
        }

        return rolUsuarioRepository.save(rolUsuario);
    }

    public List<RolUsuario> listarRolesUsuario() {
        return rolUsuarioRepository.findAll();
    }

    public List<RolUsuario> buscarPorUsuario(int idUsuario) {
        return rolUsuarioRepository.findByUsuarioId(idUsuario);
    }
}
