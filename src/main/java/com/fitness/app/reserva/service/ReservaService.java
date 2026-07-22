package com.fitness.app.reserva.service;

import com.fitness.app.clase.entity.Clase;
import com.fitness.app.clase.entity.EstadoClase;
import com.fitness.app.clase.repository.ClaseRepository;
import com.fitness.app.reserva.dto.ReservaRequestDTO;
import com.fitness.app.reserva.entity.EstadoReserva;
import com.fitness.app.reserva.entity.Reserva;
import com.fitness.app.reserva.repository.ReservaRepository;
import com.fitness.app.rolusuario.repository.RolUsuarioRepository;
import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClaseRepository claseRepository;
    private final RolUsuarioRepository rolUsuarioRepository;

    public ReservaService(
            ReservaRepository reservaRepository,
            UsuarioRepository usuarioRepository,
            ClaseRepository claseRepository,
            RolUsuarioRepository rolUsuarioRepository
    ) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.claseRepository = claseRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Reserva> listar() {
        return reservaRepository.findByActivoTrue();
    }

    @Transactional
    public Reserva guardar(
            ReservaRequestDTO dto,
            String emailUsuarioAutenticado
    ) {
        Usuario usuario =
                validarYObtenerMember(emailUsuarioAutenticado);

        Clase clase = claseRepository
                .findById(dto.getClaseId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Clase no encontrada."
                        )
                );

        validarClaseReservable(clase);

        Optional<Reserva> reservaExistente =
                reservaRepository
                        .findByUsuarioIdAndClaseId(
                                usuario.getId(),
                                clase.getId()
                        );

        if (
                reservaExistente.isPresent() &&
                        reservaExistente.get().isActivo()
        ) {
            throw new RuntimeException(
                    "Ya tienes una reserva activa para esta clase."
            );
        }

        long reservasActivas =
                reservaRepository
                        .countByClaseIdAndActivoTrue(
                                clase.getId()
                        );

        if (reservasActivas >= clase.getCupos()) {
            throw new RuntimeException(
                    "No hay cupos disponibles para esta clase."
            );
        }

        if (reservaExistente.isPresent()) {
            Reserva reserva = reservaExistente.get();

            reserva.setActivo(true);
            reserva.setEstado(
                    EstadoReserva.CONFIRMADA
            );
            reserva.setFechaReserva(
                    LocalDateTime.now()
            );

            return reservaRepository.save(reserva);
        }

        Reserva reserva = new Reserva();

        reserva.setUsuario(usuario);
        reserva.setClase(clase);
        reserva.setActivo(true);
        reserva.setFechaReserva(
                LocalDateTime.now()
        );
        reserva.setEstado(
                EstadoReserva.CONFIRMADA
        );

        return reservaRepository.save(reserva);
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarReservasPropias(String emailUsuarioAutenticado) {
        Usuario usuario = validarYObtenerMember(emailUsuarioAutenticado);
        return reservaRepository.findByUsuarioIdAndActivoTrue(usuario.getId());
    }

    @Transactional
    public void cancelarReservaPropia(
            int reservaId,
            String emailUsuarioAutenticado
    ) {
        Usuario usuario =
                validarYObtenerMember(
                        emailUsuarioAutenticado
                );

        Reserva reserva = reservaRepository
                .findByIdAndUsuarioIdAndActivoTrue(
                        reservaId,
                        usuario.getId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "La reserva no existe, ya está cancelada o no pertenece al usuario autenticado."
                        )
                );

        reserva.setActivo(false);
        reserva.setEstado(
                EstadoReserva.CANCELADA
        );

        reservaRepository.save(reserva);
    }

    private Usuario validarYObtenerMember(String emailUsuarioAutenticado) {
        Usuario usuario = usuarioRepository
                .findByEmailAndActivoTrue(emailUsuarioAutenticado)
                .orElseThrow(() -> new RuntimeException(
                        "Usuario autenticado no encontrado o inactivo."
                ));

        boolean esMember = rolUsuarioRepository
                .existsByUsuarioIdAndRolNombreAndActivoTrue(usuario.getId(), "MEMBER");

        if (!esMember) {
            throw new RuntimeException("El usuario autenticado no tiene rol MEMBER.");
        }

        return usuario;
    }

    private void validarClaseReservable(Clase clase) {
        if (!clase.isActivo()) {
            throw new RuntimeException("No se puede reservar una clase inactiva.");
        }

        if (clase.getEstado() != EstadoClase.PROGRAMADA) {
            throw new RuntimeException("Solo se pueden reservar clases programadas.");
        }

        if (clase.getFecha() == null) {
            throw new RuntimeException("La clase no tiene una fecha válida.");
        }

        if (clase.getFecha().isBefore(LocalDate.now())) {
            throw new RuntimeException("No se puede reservar una clase cuya fecha ya pasó.");
        }
    }
}