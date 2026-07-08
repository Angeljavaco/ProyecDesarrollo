package com.fitness.app.rolusuario.repository;

import com.fitness.app.rolusuario.entity.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolUsuarioRepository extends JpaRepository<RolUsuario, Integer> {

    List<RolUsuario> findByUsuarioId(int idUsuario);
}
