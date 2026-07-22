package com.fitness.app.reserva.repository;

import com.fitness.app.reserva.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    List<Reserva> findByClaseIdAndActivoTrue(int claseId);

    List<Reserva> findByActivoTrue();

    boolean existsByClaseIdAndActivoTrue(int claseId);

    boolean existsByUsuarioIdAndEstadoAndActivoTrue(
            int usuarioId,
            String estado
    );

    boolean existsByClaseIdAndEstadoAndActivoTrue(
            int claseId,
            String estado
    );

    boolean existsByUsuarioIdAndActivoTrue(int usuarioId);

    boolean existsByUsuarioIdAndClaseIdAndActivoTrue(
            int usuarioId,
            int claseId
    );

    Optional<Reserva> findByUsuarioIdAndClaseId(
            int usuarioId,
            int claseId
    );

    long countByClaseIdAndActivoTrue(int claseId);

    Optional<Reserva> findByIdAndUsuarioIdAndActivoTrue(
            int reservaId,
            int usuarioId
    );

    List<Reserva> findByUsuarioIdAndActivoTrue(int usuarioId);
}
