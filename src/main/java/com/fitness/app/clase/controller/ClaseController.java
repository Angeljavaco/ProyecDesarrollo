package com.fitness.app.clase.controller;

import com.fitness.app.clase.dto.ClaseRequestDTO;
import com.fitness.app.clase.dto.ClaseResponseDTO;
import com.fitness.app.clase.entity.Clase;
import com.fitness.app.clase.service.ClaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @GetMapping
    public List<ClaseResponseDTO> listar() {
        return claseService.listar()
                .stream()
                .map(ClaseResponseDTO::new)
                .toList();
    }

    @GetMapping("/disponibles")
    public List<ClaseResponseDTO> listarDisponibles() {
        return claseService.listarClasesDisponibles()
                .stream()
                .map(ClaseResponseDTO::new)
                .toList();
    }

    @GetMapping("/mis-clases")
    public List<ClaseResponseDTO> listarMisClases(
            Authentication authentication
    ) {
        return claseService
                .listarMisClases(authentication.getName())
                .stream()
                .map(ClaseResponseDTO::new)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ClaseResponseDTO> programarClase(
            @Valid @RequestBody ClaseRequestDTO dto,
            Authentication authentication
        ) {
        Clase clase = claseService.guardar(dto, authentication.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ClaseResponseDTO(clase));
    }

    @PutMapping("/{id}")
    public ClaseResponseDTO actualizar(
            @PathVariable int id,
            @RequestBody ClaseRequestDTO dto,
            Authentication authentication
    ) {
        Clase clase = claseService.actualizar(id, dto, authentication.getName());
        return new ClaseResponseDTO(clase);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        claseService.eliminarClase(id);
        return ResponseEntity.noContent().build();
    }
}