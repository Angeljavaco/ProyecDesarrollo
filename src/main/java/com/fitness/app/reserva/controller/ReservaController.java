package com.fitness.app.reserva.controller;

import com.fitness.app.reserva.dto.ReservaRequestDTO;
import com.fitness.app.reserva.dto.ReservaResponseDTO;
import com.fitness.app.reserva.entity.Reserva;
import com.fitness.app.reserva.service.ReservaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private static final Logger log = LoggerFactory.getLogger(ReservaController.class);
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listar() {
        log.info("📋 Listando todas las reservas");
        List<ReservaResponseDTO> reservas = reservaService.listar()
                .stream()
                .map(ReservaResponseDTO::new)
                .toList();
        log.info("✅ Se encontraron {} reservas", reservas.size());
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/mis-reservas")
    public ResponseEntity<List<ReservaResponseDTO>> listarMisReservas(
            Authentication authentication
    ) {
        String email = authentication.getName();
        log.info("📋 Listando reservas del usuario: {}", email);

        List<ReservaResponseDTO> reservas = reservaService
                .listarReservasPropias(email)
                .stream()
                .map(ReservaResponseDTO::new)
                .toList();

        log.info("✅ Usuario {} tiene {} reservas activas", email, reservas.size());
        return ResponseEntity.ok(reservas);
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crear(
            @Valid @RequestBody ReservaRequestDTO dto,
            Authentication authentication
    ) {
        String email = authentication.getName();
        log.info("📝 Creando reserva para clase ID: {} por usuario: {}",
                dto.getClaseId(), email);

        Reserva reserva = reservaService.guardar(dto, email);

        log.info("✅ Reserva creada exitosamente ID: {} para usuario: {}",
                reserva.getId(), email);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ReservaResponseDTO(reserva));
    }

    @DeleteMapping("/{reservaId}")
    public ResponseEntity<Void> cancelarReserva(
            @PathVariable int reservaId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        log.info("❌ Cancelando reserva ID: {} por usuario: {}", reservaId, email);

        reservaService.cancelarReservaPropia(reservaId, email);

        log.info("✅ Reserva ID: {} cancelada exitosamente por usuario: {}",
                reservaId, email);

        return ResponseEntity.noContent().build();
    }
}