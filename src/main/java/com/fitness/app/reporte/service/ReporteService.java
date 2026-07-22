package com.fitness.app.reporte.service;

import com.fitness.app.clase.entity.Clase;
import com.fitness.app.clase.repository.ClaseRepository;
import com.fitness.app.reporte.dto.ReporteClaseDTO;
import com.fitness.app.reporte.dto.ReporteInscritoDTO;
import com.fitness.app.reserva.entity.Reserva;
import com.fitness.app.reserva.repository.ReservaRepository;
import com.fitness.app.rolusuario.repository.RolUsuarioRepository;
import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReporteService {

    private final ClaseRepository claseRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolUsuarioRepository rolUsuarioRepository;

    public ReporteService(
            ClaseRepository claseRepository,
            ReservaRepository reservaRepository,
            UsuarioRepository usuarioRepository,
            RolUsuarioRepository rolUsuarioRepository
    ) {
        this.claseRepository = claseRepository;
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
    }

    /**
     * Genera el reporte de inscritos de una clase.
     *
     * Reglas:
     * 1. El usuario autenticado debe existir y estar activo.
     * 2. Debe tener rol TRAINER.
     * 3. La clase debe existir y estar activa.
     * 4. La clase debe pertenecer al trainer autenticado.
     * 5. Solo se consideran reservas activas.
     */
    @Transactional(readOnly = true)
    public ReporteClaseDTO generarReporteInscritos(
            int claseId,
            String emailTrainerAutenticado
    ) {
        Usuario trainer =
                validarYObtenerTrainer(emailTrainerAutenticado);

        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Clase no encontrada."
                        )
                );

        if (!clase.isActivo()) {
            throw new RuntimeException(
                    "No se puede generar el reporte de una clase inactiva."
            );
        }

        if (clase.getTrainer() == null) {
            throw new RuntimeException(
                    "La clase no tiene un trainer asignado."
            );
        }

        if (clase.getTrainer().getId() != trainer.getId()) {
            throw new RuntimeException(
                    "No puedes consultar inscritos de una clase que pertenece a otro trainer."
            );
        }

        List<Reserva> reservasActivas =
                reservaRepository.findByClaseIdAndActivoTrue(
                        clase.getId()
                );

        List<ReporteInscritoDTO> inscritos =
                reservasActivas.stream()
                        .map(this::convertirAInscritoDTO)
                        .toList();

        int cuposTotales = clase.getCupos();
        int totalInscritos = inscritos.size();

        int cuposDisponibles =
                Math.max(
                        cuposTotales - totalInscritos,
                        0
                );

        return new ReporteClaseDTO(
                clase.getId(),
                clase.getNombre(),
                clase.getDescripcion(),
                clase.getFecha(),
                clase.getEstado().name(),
                trainer.getId(),
                trainer.getNombre(),
                cuposTotales,
                totalInscritos,
                cuposDisponibles,
                inscritos
        );
    }

    private Usuario validarYObtenerTrainer(
            String emailTrainerAutenticado
    ) {
        Usuario trainer = usuarioRepository
                .findByEmailAndActivoTrue(
                        emailTrainerAutenticado
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Trainer autenticado no encontrado o inactivo."
                        )
                );

        boolean esTrainer =
                rolUsuarioRepository
                        .existsByUsuarioIdAndRolNombreAndActivoTrue(
                                trainer.getId(),
                                "TRAINER"
                        );

        if (!esTrainer) {
            throw new RuntimeException(
                    "El usuario autenticado no tiene rol TRAINER."
            );
        }

        return trainer;
    }

    private ReporteInscritoDTO convertirAInscritoDTO(
            Reserva reserva
    ) {
        Usuario usuario = reserva.getUsuario();

        if (usuario == null) {
            throw new RuntimeException(
                    "La reserva no tiene un usuario asociado."
            );
        }

        return new ReporteInscritoDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTelefono()
        );
    }
}