package com.fitness.app.clase.repository;

import com.fitness.app.clase.entity.Clase;
import com.fitness.app.clase.entity.EstadoClase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface ClaseRepository extends JpaRepository<Clase, Integer> {

    List<Clase> findByActivoTrue();

    boolean existsByTrainerIdAndActivoTrueAndFechaGreaterThanEqual(
            int trainerId,
            LocalDate fecha
    );

    boolean existsByTrainerIdAndActivoTrue(int trainerId);

    List<Clase> findByActivoTrueAndEstadoAndFechaGreaterThanEqual(
            EstadoClase estado,
            LocalDate fecha
    );

    List<Clase> findByTrainerIdAndActivoTrue(int trainerId);

}
