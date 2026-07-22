package com.fitness.app.rolusuario.service;

import com.fitness.app.rol.entity.Rol;
import com.fitness.app.rol.repository.RolRepository;
import com.fitness.app.rolusuario.dto.RolUsuarioRequestDTO;
import com.fitness.app.rolusuario.entity.RolUsuario;
import com.fitness.app.rolusuario.repository.RolUsuarioRepository;
import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolUsuarioService {

    private final RolUsuarioRepository rolUsuarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public RolUsuarioService(
            RolUsuarioRepository rolUsuarioRepository,
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository
    ) {
        this.rolUsuarioRepository = rolUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    public RolUsuario asignarRol(RolUsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        if (!usuario.isActivo()) {
            throw new RuntimeException("No se puede asignar rol a un usuario inactivo.");
        }

        if (rolUsuarioRepository.existsByUsuarioIdAndRolIdAndActivoTrue(dto.getUsuarioId(), dto.getRolId())) {
            throw new RuntimeException("El usuario ya tiene ese rol asignado.");
        }

        RolUsuario rolUsuario = new RolUsuario();
        rolUsuario.setUsuario(usuario);
        rolUsuario.setRol(rol);
        rolUsuario.setActivo(true);

        return rolUsuarioRepository.save(rolUsuario);
    }

    public List<RolUsuario> listarRolesUsuario() {
        return rolUsuarioRepository.findByActivoTrue();
    }

    public List<RolUsuario> buscarPorUsuario(int usuarioId) {
        return rolUsuarioRepository.findByUsuarioIdAndActivoTrue(usuarioId);
    }

    public void eliminarRolUsuario(int id) {
        RolUsuario rolUsuario = rolUsuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación de rol no encontrada"));

        rolUsuario.setActivo(false);
        rolUsuarioRepository.save(rolUsuario);
    }
}