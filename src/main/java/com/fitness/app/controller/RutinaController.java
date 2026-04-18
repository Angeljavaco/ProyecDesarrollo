package com.fitness.app.controller;

import com.fitness.app.model.Rutina;
import com.fitness.app.service.RutinaService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rutinas")
public class RutinaController {

    private final RutinaService service;

    public RutinaController(RutinaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Rutina>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PostMapping
    public ResponseEntity<Rutina> guardar(@Valid @RequestBody Rutina r) {
        return ResponseEntity.ok(service.guardar(r));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rutina> buscar(@PathVariable int id) {
        Rutina r = service.buscar(id);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(r);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rutina> actualizar(@PathVariable int id, @Valid @RequestBody Rutina r) {
        Rutina actualizada = service.actualizar(id, r);
        if (actualizada == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        boolean eliminado = service.eliminar(id);
        if (!eliminado) return ResponseEntity.notFound().build();
        return ResponseEntity.ok("Eliminado");
    }

    @GetMapping("/tipo/{tipo}")
    public List<Rutina> buscarPorTipo(@PathVariable String tipo) {
        return service.listar().stream()
                .filter(r -> r.getTipo().equalsIgnoreCase(tipo))
                .toList();
    }

    @GetMapping("/duracion/{min}")
    public List<Rutina> filtrarPorDuracion(@PathVariable int min) {
        return service.listar().stream()
                .filter(r -> r.getDuracion() >= min)
                .toList();
    }

    @GetMapping("/count")
    public int contar() {
        return service.listar().size();
    }
    @GetMapping("/nombre/{nombre}")
public List<Rutina> filtrarPorNombre(@PathVariable String nombre) {
    return service.listar().stream()
            .filter(r -> r.getNombre().equalsIgnoreCase(nombre))
            .toList();
}
}