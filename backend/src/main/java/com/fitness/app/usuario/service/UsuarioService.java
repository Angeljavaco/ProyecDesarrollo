package com.fitness.app.usuario.service;

import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);
    private static final Pattern BCRYPT_PATTERN =
            Pattern.compile("\\A\\$2[aby]\\$\\d\\d\\$[./A-Za-z0-9]{53}\\z");

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario crearUsuario(Usuario usuario) {
        if (usuario.getEmail() != null) {
            usuario.setEmail(usuario.getEmail().trim());
        }

        String password = usuario.getPassword();

        if (password != null && !isBcryptHash(password)) {
            log.info("USUARIO CREATE - Codificando password con BCrypt para email={}",
                    usuario.getEmail());
            usuario.setPassword(passwordEncoder.encode(password));
        } else {
            log.info("USUARIO CREATE - Password ya parece BCrypt; se evita doble encriptacion para email={}",
                    usuario.getEmail());
        }

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public void eliminarUsuario(int id) {
        usuarioRepository.deleteById(id);
    }

    private boolean isBcryptHash(String password) {
        return password != null && BCRYPT_PATTERN.matcher(password).matches();
    }
}
