package com.fitness.app.usuario.repository;

import com.fitness.app.rolusuario.entity.RolUsuario;
import com.fitness.app.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findByActivoTrue();

    boolean existsByIdAndActivoTrue(int id);

    Optional<Usuario> findByEmailAndActivoTrue(String email);

    Optional<Usuario> findByEmail(String email);

}