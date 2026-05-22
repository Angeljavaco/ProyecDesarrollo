package com.fitness.app.auth.service;

import com.fitness.app.auth.dto.AuthRequest;
import com.fitness.app.auth.dto.AuthResponse;
import com.fitness.app.security.JwtService;
import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
                       JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    public AuthResponse login(AuthRequest request) {

        Usuario usuario = usuarioRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Password incorrecto");
        }

        String token = jwtService.generarToken(usuario.getEmail());

        return new AuthResponse(token);
    }
}