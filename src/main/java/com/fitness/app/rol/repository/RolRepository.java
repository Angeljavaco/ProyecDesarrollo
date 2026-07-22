package com.fitness.app.rol.repository;

import com.fitness.app.rol.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findById(int id);
    Optional<Rol> findByNombre(String nombre);

}