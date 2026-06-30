package com.fitness.app.Auth;

import com.fitness.app.auth.dto.AuthRequest;
import com.fitness.app.auth.dto.AuthResponse;
import com.fitness.app.auth.dto.PasswordResetRequest;
import com.fitness.app.auth.service.AuthService;
import com.fitness.app.security.JwtService;
import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UsuarioRepository usuarioRepository;
    private AuthService authService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        JwtService jwtService = mock(JwtService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(usuarioRepository, jwtService, passwordEncoder);

        when(jwtService.generarToken("yeison@gmail.com")).thenReturn("jwt-token");
    }

    @Test
    void loginPermitePasswordPlanoCuandoLaBaseTieneHashBCrypt() {
        String hash = passwordEncoder.encode("123456");
        when(usuarioRepository.findByEmail("yeison@gmail.com"))
                .thenReturn(Optional.of(usuario("yeison@gmail.com", hash)));

        AuthRequest request = new AuthRequest();
        request.setEmail("yeison@gmail.com");
        request.setPassword("123456");

        AuthResponse response = authService.login(request);

        assertEquals("jwt-token", response.getToken());
    }

    @Test
    void loginRechazaIngresarDirectamenteElHashBCrypt() {
        String hash = passwordEncoder.encode("123456");
        when(usuarioRepository.findByEmail("yeison@gmail.com"))
                .thenReturn(Optional.of(usuario("yeison@gmail.com", hash)));

        AuthRequest request = new AuthRequest();
        request.setEmail("yeison@gmail.com");
        request.setPassword(hash);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> authService.login(request)
        );

        assertEquals(401, exception.getStatusCode().value());
    }

    @Test
    void reasignarPasswordTemporalCodificaYGuardaBCrypt() {
        Usuario usuario = usuario("yeison@gmail.com", "password-antiguo");
        when(usuarioRepository.findByEmail("yeison@gmail.com")).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail("yeison@gmail.com");
        request.setNuevaPassword("123456");

        Map<String, Object> response = authService.reasignarPasswordTemporal(request);
        String hashGenerado = (String) response.get("hashBCryptGenerado");

        assertTrue(hashGenerado.startsWith("$2"));
        assertTrue(passwordEncoder.matches("123456", hashGenerado));
        assertEquals(hashGenerado, usuario.getPassword());
        verify(usuarioRepository).save(usuario);
    }

    private Usuario usuario(String email, String password) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setNombre("Usuario Test");
        usuario.setTelefono("999999999");
        return usuario;
    }
}
