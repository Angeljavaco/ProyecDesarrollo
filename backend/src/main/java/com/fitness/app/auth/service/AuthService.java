package com.fitness.app.auth.service;

import com.fitness.app.auth.dto.AuthRequest;
import com.fitness.app.auth.dto.AuthResponse;
import com.fitness.app.auth.dto.PasswordResetRequest;
import com.fitness.app.security.JwtService;
import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final Pattern BCRYPT_PATTERN =
            Pattern.compile("\\A\\$2[aby]\\$\\d\\d\\$[./A-Za-z0-9]{53}\\z");

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthRequest request) {

        String email = request.getEmail() == null ? null : request.getEmail().trim();
        String rawPassword = request.getPassword();

        log.info("AUTH LOGIN - Intento de login para email={}", email);

        if (email == null || email.isBlank() || rawPassword == null) {
            log.warn("AUTH LOGIN - Rechazado: email o password vacio");
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Email o password vacio"
            );
        }

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(email);
        log.info("AUTH LOGIN - Usuario encontrado para email={}: {}",
                email,
                usuarioEncontrado.isPresent());

        Usuario usuario = usuarioEncontrado.orElseThrow(() -> {
            log.warn("AUTH LOGIN - Rechazado: usuario no encontrado para email={}", email);
            return new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Usuario no encontrado"
            );
        });

        String storedHash = usuario.getPassword();
        boolean bcryptHash = isBcryptHash(storedHash);
        boolean rawPasswordLooksLikeBcrypt = isBcryptHash(rawPassword);
        boolean passwordMatches = bcryptHash
                && passwordEncoder.matches(rawPassword, storedHash);

        log.info("AUTH LOGIN - Hash almacenado para email={}: {}", email, storedHash);
        log.info("AUTH LOGIN - Password ingresado parece BCrypt para email={}: {}",
                email,
                rawPasswordLooksLikeBcrypt);
        log.info("AUTH LOGIN - Hash BCrypt compatible para email={}: {}", email, bcryptHash);
        log.info("AUTH LOGIN - Resultado passwordEncoder.matches() para email={}: {}",
                email,
                passwordMatches);

        if (rawPasswordLooksLikeBcrypt) {
            log.warn("AUTH LOGIN - Rechazado: se intento usar un hash BCrypt como password para email={}",
                    email);
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "No se permite usar un hash BCrypt como password"
            );
        }

        if (!bcryptHash) {
            log.warn("AUTH LOGIN - Rechazado: password almacenado no es BCrypt para email={}", email);
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Password almacenado no es BCrypt"
            );
        }

        if (!passwordMatches) {
            log.warn("AUTH LOGIN - Rechazado: password no coincide para email={}", email);
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciales incorrectas"
            );
        }

        String token = jwtService.generarToken(usuario.getEmail());

        return new AuthResponse(token);
    }

    public Map<String, Object> reasignarPasswordTemporal(PasswordResetRequest request) {
        String email = request.getEmail() == null ? null : request.getEmail().trim();
        String nuevaPassword = request.getNuevaPassword();

        log.info("AUTH RESET TEMP - Solicitud de reasignacion para email={}", email);

        if (email == null || email.isBlank() || nuevaPassword == null || nuevaPassword.isBlank()) {
            log.warn("AUTH RESET TEMP - Rechazado: email o nuevaPassword vacio");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email y nuevaPassword son requeridos"
            );
        }

        if (isBcryptHash(nuevaPassword)) {
            log.warn("AUTH RESET TEMP - Rechazado: nuevaPassword parece hash BCrypt para email={}", email);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La nuevaPassword debe ser texto plano, no un hash BCrypt"
            );
        }

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("AUTH RESET TEMP - Rechazado: usuario no encontrado para email={}", email);
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Usuario no encontrado"
                    );
                });

        String hashGenerado = passwordEncoder.encode(nuevaPassword);
        usuario.setPassword(hashGenerado);
        usuarioRepository.save(usuario);

        boolean matches = passwordEncoder.matches(nuevaPassword, hashGenerado);

        log.info("AUTH RESET TEMP - Hash generado para email={}: {}", email, hashGenerado);
        log.info("AUTH RESET TEMP - Verificacion passwordEncoder.matches() para email={}: {}",
                email,
                matches);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("email", email);
        resultado.put("hashBCryptGenerado", hashGenerado);
        resultado.put("matches", matches);
        resultado.put("mensaje", "Password reasignado temporalmente");

        return resultado;
    }

    public Map<String, Object> generarHashPrueba(String password) {
        if (password == null || password.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password requerido"
            );
        }

        String hash = passwordEncoder.encode(password);
        boolean matches = passwordEncoder.matches(password, hash);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("passwordIngresada", password);
        resultado.put("hashBCrypt", hash);
        resultado.put("matches", matches);

        log.info("AUTH BCRYPT TEST - Hash BCrypt generado. matches={}", matches);

        return resultado;
    }

    private boolean isBcryptHash(String password) {
        return password != null && BCRYPT_PATTERN.matcher(password).matches();
    }
}
