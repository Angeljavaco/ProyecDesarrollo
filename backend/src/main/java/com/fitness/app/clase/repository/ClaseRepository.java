package com.fitness.app.clase.repository;

import com.fitness.app.clase.entity.Clase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaseRepository extends JpaRepository<Clase, Integer> {
}
