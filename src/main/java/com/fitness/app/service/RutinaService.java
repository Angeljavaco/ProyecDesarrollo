package com.fitness.app.service;

import com.fitness.app.model.Rutina;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RutinaService {

    private List<Rutina> lista = new ArrayList<>();

    public List<Rutina> listar() {
        return new ArrayList<>(lista);
    }

    public Rutina guardar(Rutina r) {
        if (buscar(r.getId()) != null) {
            return null; // o lanzar excepción
        }
        lista.add(r);
        return r;
    }

    public Rutina buscar(int id) {
        return lista.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Rutina actualizar(int id, Rutina nueva) {
        Rutina r = buscar(id);
        if (r != null) {
            r.setNombre(nueva.getNombre());
            r.setTipo(nueva.getTipo());
            r.setDuracion(nueva.getDuracion());
            r.setId(id); // asegurar consistencia
        }
        return r;
    }

    public void limpiar() {
        lista.clear();
    }

    public boolean eliminar(int id) {
        return lista.removeIf(r -> r.getId() == id);
    }

}