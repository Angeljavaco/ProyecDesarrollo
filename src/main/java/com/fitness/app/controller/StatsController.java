package com.fitness.app.controller;

import com.fitness.app.service.RutinaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final RutinaService service;

    public StatsController(RutinaService service) {
        this.service = service;
    }

    // 1
    @GetMapping("/total")
    public int totalRutinas() {
        return service.listar().size();
    }

    // 2
    @GetMapping("/duracionPromedio")
    public double promedioDuracion() {
        return service.listar().stream()
                .mapToInt(r -> r.getDuracion())
                .average()
                .orElse(0);
    }

    // 3
    @GetMapping("/max")
    public int maxDuracion() {
        return service.listar().stream()
                .mapToInt(r -> r.getDuracion())
                .max()
                .orElse(0);
    }
}