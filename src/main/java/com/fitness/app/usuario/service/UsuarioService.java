package com.fitness.app.usuario.service;

import com.fitness.app.clase.repository.ClaseRepository;
import com.fitness.app.reserva.repository.ReservaRepository;
import com.fitness.app.rolusuario.entity.RolUsuario;
import com.fitness.app.rolusuario.repository.RolUsuarioRepository;
import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClaseRepository claseRepository;
    private final ReservaRepository reservaRepository;
    private final RolUsuarioRepository rolUsuarioRepository;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            ClaseRepository claseRepository,
            ReservaRepository reservaRepository,
            RolUsuarioRepository rolUsuarioRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.claseRepository = claseRepository;
        this.reservaRepository = reservaRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
    }

    public Usuario crearUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado.");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findByActivoTrue();
    }

    public void eliminarUsuario(int id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (claseRepository.existsByTrainerIdAndActivoTrue(id)) {
            throw new RuntimeException("No se puede eliminar el usuario porque tiene clases activas como trainer.");
        }

        if (reservaRepository.existsByUsuarioIdAndActivoTrue(id)) {
            throw new RuntimeException("No se puede eliminar el usuario porque tiene reservas activas.");
        }

        usuario.setActivo(false);
        usuarioRepository.save(usuario);

        List<RolUsuario> rolesUsuario = rolUsuarioRepository.findByUsuarioIdAndActivoTrue(id);

        for (RolUsuario rolUsuario : rolesUsuario) {
            rolUsuario.setActivo(false);
        }

        rolUsuarioRepository.saveAll(rolesUsuario);
    }
}