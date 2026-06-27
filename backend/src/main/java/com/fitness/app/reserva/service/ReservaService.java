package com.fitness.app.reserva.service;

import com.fitness.app.reserva.entity.Reserva;
import com.fitness.app.reserva.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    public Reserva guardar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }
}