package com.fitness.app.auth.controller;

import com.fitness.app.auth.dto.AuthRequest;
import com.fitness.app.auth.dto.AuthResponse;
import com.fitness.app.auth.dto.PasswordResetRequest;
import com.fitness.app.auth.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        return authService.login(request);
    }

    @PostMapping("/bcrypt-test")
    public Map<String, Object> generarHashPrueba(@RequestBody AuthRequest request) {
        return authService.generarHashPrueba(request.getPassword());
    }

    @PostMapping("/reset-password-temporal")
    public Map<String, Object> reasignarPasswordTemporal(@RequestBody PasswordResetRequest request) {
        return authService.reasignarPasswordTemporal(request);
    }
}
