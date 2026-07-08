package com.fitness.app.security;

import com.fitness.app.rol.entity.Rol;
import com.fitness.app.rol.repository.RolRepository;
import com.fitness.app.rolusuario.entity.RolUsuario;
import com.fitness.app.rolusuario.repository.RolUsuarioRepository;
import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UsuarioRepository usuarioRepository;
    private final RolUsuarioRepository rolUsuarioRepository;
    private final RolRepository rolRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository,
                                    RolUsuarioRepository rolUsuarioRepository,
                                    RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String emailNormalizado = email == null ? null : email.trim();

        Usuario usuario = usuarioRepository.findByEmail(emailNormalizado)
                .orElseThrow(() -> {
                    log.warn("USER DETAILS - Usuario no encontrado para email={}", emailNormalizado);
                    return new UsernameNotFoundException("Usuario no encontrado");
        });

        List<SimpleGrantedAuthority> authorities = rolUsuarioRepository
                .findByUsuarioId(usuario.getId())
                .stream()
                .map(RolUsuario::getIdRol)
                .map(rolRepository::findById)
                .flatMap(Optional::stream)
                .map(Rol::getNombre)
                .map(SimpleGrantedAuthority::new)
                .toList();

        log.info("USER DETAILS - Usuario cargado para email={} con {} authorities",
                emailNormalizado,
                authorities.size());

        return User.withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(authorities)
                .build();
    }
}
