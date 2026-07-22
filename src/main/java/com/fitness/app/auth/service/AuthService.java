package com.fitness.app.auth.service;

import com.fitness.app.auth.dto.AuthRequest;
import com.fitness.app.auth.dto.AuthResponse;
import com.fitness.app.rol.entity.Rol;
import com.fitness.app.rolusuario.entity.RolUsuario;
import com.fitness.app.rolusuario.repository.RolUsuarioRepository;
import com.fitness.app.security.JwtService;
import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RolUsuarioRepository rolUsuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository,
                       JwtService jwtService, PasswordEncoder passwordEncoder, RolUsuarioRepository rolUsuarioRepository) {

        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.rolUsuarioRepository = rolUsuarioRepository;
    }

    public AuthResponse login(AuthRequest request) {

        Usuario usuario = usuarioRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException
                            (HttpStatus.UNAUTHORIZED,
                            "Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciales incorrectas"
            );
        }

        List<String> roles = rolUsuarioRepository
                .findByUsuarioId(usuario.getId())
                .stream()
                .filter(RolUsuario::isActivo)
                .map(rolUsuario ->
                        rolUsuario
                                .getRol()
                                .getNombre()
                )
                .toList();

        String token = jwtService.generarToken(usuario.getEmail(), roles);

        return new AuthResponse(token);
    }
}