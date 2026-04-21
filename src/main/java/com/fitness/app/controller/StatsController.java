package com.fitness.app.controller;

import com.fitness.app.model.Rutina;
import com.fitness.app.service.RutinaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stats")
public class StatsController {
    private final RutinaService service;
    public StatsController(RutinaService service) {
        this.service = service;
    }

    @GetMapping("/total")
    public ResponseEntity<Integer> totalRutinas() {
        int total = service.listar().size();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/duracionPromedio")
    public ResponseEntity<Double> promedioDuracion() {
        double promedio = service.listar().stream()
                .mapToInt(Rutina::getDuracion)
                .average()
                .orElse(0);

        return ResponseEntity.ok(promedio);
    }

    @GetMapping("/max")
    public ResponseEntity<Integer> maxDuracion() {
        int max = service.listar().stream()
                .mapToInt(Rutina::getDuracion)
                .max()
                .orElse(0);

        return ResponseEntity.ok(max);
    }
}