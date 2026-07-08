package com.fitness.app.clase.controller;

import com.fitness.app.clase.entity.Clase;
import com.fitness.app.clase.service.ClaseService;
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
    public List<Clase> listar() {
        return claseService.listar();
    }

    @PostMapping
    public Clase guardar(@RequestBody Clase clase) {
        return claseService.guardar(clase);
    }

    @PutMapping("/{id}")
    public Clase actualizar(@PathVariable int id,
                            @RequestBody Clase clase) {

        return claseService.actualizar(id, clase);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable int id) {
        claseService.eliminar(id);
    }
}