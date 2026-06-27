package com.fitness.app.rolusuario.service;

import com.fitness.app.rolusuario.entity.RolUsuario;
import com.fitness.app.rolusuario.repository.RolUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolUsuarioService {

    private final RolUsuarioRepository rolUsuarioRepository;

    public RolUsuarioService(RolUsuarioRepository rolUsuarioRepository) {
        this.rolUsuarioRepository = rolUsuarioRepository;
    }

    public RolUsuario asignarRol(RolUsuario rolUsuario) {
        return rolUsuarioRepository.save(rolUsuario);
    }

    public List<RolUsuario> listarRolesUsuario() {
        return rolUsuarioRepository.findAll();
    }

    public List<RolUsuario> buscarPorUsuario(int idUsuario) {
        return rolUsuarioRepository.findByIdUsuario(idUsuario);
    }
}