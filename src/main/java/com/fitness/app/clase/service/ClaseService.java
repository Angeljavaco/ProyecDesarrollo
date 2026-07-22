package com.fitness.app.clase.service;

import com.fitness.app.clase.dto.ClaseRequestDTO;
import com.fitness.app.clase.entity.Clase;
import com.fitness.app.clase.entity.EstadoClase;
import com.fitness.app.clase.repository.ClaseRepository;
import com.fitness.app.reserva.repository.ReservaRepository;
import com.fitness.app.rolusuario.repository.RolUsuarioRepository;
import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolUsuarioRepository rolUsuarioRepository;

    public ClaseService(
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

    public List<Clase> listar() {
        return claseRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public List<Clase> listarClasesDisponibles() {
        return claseRepository
                .findByActivoTrueAndEstadoAndFechaGreaterThanEqual(
                        EstadoClase.PROGRAMADA,
                        LocalDate.now()
                );
    }

    @Transactional(readOnly = true)
    public List<Clase> listarMisClases(
            String emailTrainerAutenticado
    ) {
        Usuario trainer =
                validarYObtenerTrainer(emailTrainerAutenticado);

        return claseRepository.findByTrainerIdAndActivoTrue(
                trainer.getId()
        );
    }

    private Usuario validarYObtenerTrainer(String email) {
        Usuario trainer = usuarioRepository.findByEmailAndActivoTrue(email)
                .orElseThrow(() -> new RuntimeException("Trainer no encontrado o inactivo"));

        boolean esTrainer = rolUsuarioRepository.existsByUsuarioIdAndRolNombreAndActivoTrue(
                trainer.getId(), "TRAINER");

        if (!esTrainer) {
            throw new RuntimeException("El usuario no tiene rol TRAINER.");
        }

        return trainer;
    }

    private void validarFechaClase(LocalDate fecha) {
        if (fecha == null) {
            throw new RuntimeException("La fecha de la clase es obligatoria.");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new RuntimeException("No se puede programar una clase en el pasado.");
        }
    }

    @Transactional
    public Clase guardar(ClaseRequestDTO dto, String emailTrainerAutenticado) {
        Usuario trainer = validarYObtenerTrainer(emailTrainerAutenticado);

        validarFechaClase(dto.getFecha());

        Clase clase = new Clase();
        clase.setNombre(dto.getNombre().trim());
        clase.setDescripcion(dto.getDescripcion().trim());
        clase.setTrainer(trainer);
        clase.setCupos(dto.getCupos());
        clase.setActivo(true);
        clase.setFecha(dto.getFecha());
        clase.setEstado(EstadoClase.PROGRAMADA);

        return claseRepository.save(clase);
    }

    @Transactional
    public Clase actualizar(int id, ClaseRequestDTO dto, String emailTrainerAutenticado) {
        Clase existente = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        if (!existente.isActivo()) {
            throw new RuntimeException("No se puede actualizar una clase inactiva.");
        }

        Usuario trainer = validarYObtenerTrainer(emailTrainerAutenticado);

        if (existente.getTrainer().getId() != trainer.getId()) {
            throw new RuntimeException("No puedes actualizar clases de otro trainer.");
        }

        if (existente.getEstado() != EstadoClase.PROGRAMADA) {
            throw new RuntimeException(
                    "Solo se pueden actualizar clases programadas."
            );
        }

        long reservasActivas =
                reservaRepository.countByClaseIdAndActivoTrue(id);

        if (dto.getCupos() < reservasActivas) {
            throw new RuntimeException(
                    "Los cupos no pueden ser menores que las reservas activas: "
                            + reservasActivas
            );
        }

        validarFechaClase(dto.getFecha());

        existente.setNombre(dto.getNombre().trim());
        existente.setDescripcion(dto.getDescripcion().trim());
        existente.setCupos(dto.getCupos());
        existente.setFecha(dto.getFecha());

        return claseRepository.save(existente);
    }

    public void eliminarClase(int id) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        if (reservaRepository.existsByClaseIdAndActivoTrue(id)) {
            throw new RuntimeException("No se puede eliminar la clase porque tiene reservas activas.");
        }

        clase.setActivo(false);
        claseRepository.save(clase);
    }
}