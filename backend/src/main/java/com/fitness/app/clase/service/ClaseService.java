package com.fitness.app.clase.service;

import com.fitness.app.clase.entity.Clase;
import com.fitness.app.clase.repository.ClaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;

    public ClaseService(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    public List<Clase> listar() {
        return claseRepository.findAll();
    }

    public Clase guardar(Clase clase) {
        return claseRepository.save(clase);
    }

    public Clase actualizar(int id, Clase clase) {

        Clase existente = claseRepository.findById(id).orElseThrow();

        existente.setNombre(clase.getNombre());
        existente.setDescripcion(clase.getDescripcion());
        existente.setTrainer(clase.getTrainer());
        existente.setCupos(clase.getCupos());

        return claseRepository.save(existente);
    }

    public void eliminar(int id) {
        claseRepository.deleteById(id);
    }
}