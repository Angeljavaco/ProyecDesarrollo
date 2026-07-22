package com.fitness.app.rolusuario.repository;

import com.fitness.app.rolusuario.entity.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolUsuarioRepository extends JpaRepository<RolUsuario, Integer> {

    List<RolUsuario> findByActivoTrue();

    List<RolUsuario> findByUsuarioId(int usuarioId);

    List<RolUsuario> findByUsuarioIdAndActivoTrue(int usuarioId);

    boolean existsByUsuarioIdAndActivoTrue(int usuarioId);

    boolean existsByUsuarioIdAndRolIdAndActivoTrue(int usuarioId, int rolId);

    boolean existsByUsuarioIdAndRolNombreAndActivoTrue(int usuarioId, String rolNombre);

    Optional<RolUsuario> findByUsuarioIdAndRolId(int usuarioId, int rolId);

}