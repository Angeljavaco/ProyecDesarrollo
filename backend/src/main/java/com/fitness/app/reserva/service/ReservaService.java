package com.fitness.app.reserva.service;

import com.fitness.app.clase.entity.Clase;
import com.fitness.app.reserva.entity.Reserva;
import com.fitness.app.reserva.repository.ReservaRepository;
import com.fitness.app.usuario.entity.Usuario;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final EntityManager entityManager;

    public ReservaService(ReservaRepository reservaRepository,
                          EntityManager entityManager) {
        this.reservaRepository = reservaRepository;
        this.entityManager = entityManager;
    }

    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    @Transactional
    public Reserva guardar(Reserva reserva) {
        int idUsuario = reserva.getIdUsuario();
        int idClase = reserva.getIdClase();

        if (idUsuario > 0) {
            reserva.setUsuario(entityManager.getReference(Usuario.class, idUsuario));
        }

        if (idClase > 0) {
            reserva.setClase(entityManager.getReference(Clase.class, idClase));
        }

        return reservaRepository.save(reserva);
    }
}
