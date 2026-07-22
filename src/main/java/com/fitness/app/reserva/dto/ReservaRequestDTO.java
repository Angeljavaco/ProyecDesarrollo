package com.fitness.app.reserva.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ReservaRequestDTO {

    @NotNull(message = "El ID de la clase es obligatorio")
    @Positive(message = "El ID de la clase debe ser un número positivo")
    private int claseId;

    public int getClaseId() {
        return claseId;
    }

    public void setClaseId(int claseId) {
        this.claseId = claseId;
    }

}
