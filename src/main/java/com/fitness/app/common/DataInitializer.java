package com.fitness.app.common;

import com.fitness.app.rol.entity.Rol;
import com.fitness.app.rol.repository.RolRepository;
import com.fitness.app.rolusuario.entity.RolUsuario;
import com.fitness.app.rolusuario.repository.RolUsuarioRepository;
import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolUsuarioRepository rolUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            RolUsuarioRepository rolUsuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Rol adminRol = crearRolSiNoExiste("ADMIN");
        crearRolSiNoExiste("TRAINER");
        crearRolSiNoExiste("MEMBER");

        Usuario admin = crearAdminSiNoExiste();

        asignarRolSiNoExiste(admin, adminRol);
    }

    private Rol crearRolSiNoExiste(
            String nombre
    ) {
        return rolRepository
                .findByNombre(nombre)
                .orElseGet(() -> {
                    Rol rol = new Rol();
                    rol.setNombre(nombre);

                    return rolRepository.save(rol);
                });
    }

    private Usuario crearAdminSiNoExiste() {
        return usuarioRepository.findByEmail("admin@test.com")
                .orElseGet(() -> {
                    Usuario admin = new Usuario();
                    admin.setNombre("Admin");
                    admin.setEmail("admin@test.com");
                    admin.setPassword(passwordEncoder.encode("123456"));
                    admin.setTelefono("999999999");
                    admin.setActivo(true);
                    return usuarioRepository.save(admin);
                });
    }

    private void asignarRolSiNoExiste(Usuario usuario, Rol rol) {
        boolean existe = rolUsuarioRepository.existsByUsuarioIdAndRolIdAndActivoTrue(
                usuario.getId(),
                rol.getId()
        );

        if (!existe) {
            RolUsuario rolUsuario = new RolUsuario();
            rolUsuario.setUsuario(usuario);
            rolUsuario.setRol(rol);
            rolUsuario.setActivo(true);
            rolUsuarioRepository.save(rolUsuario);
        }
    }
}
