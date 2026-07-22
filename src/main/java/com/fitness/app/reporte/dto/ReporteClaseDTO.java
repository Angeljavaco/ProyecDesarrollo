package com.fitness.app.reporte.dto;

import java.time.LocalDate;
import java.util.List;

public class ReporteClaseDTO {

    private int claseId;
    private String claseNombre;
    private String descripcion;
    private LocalDate fecha;
    private String estado;

    private int trainerId;
    private String trainerNombre;

    private int cuposTotales;
    private int totalInscritos;
    private int cuposDisponibles;

    private List<ReporteInscritoDTO> inscritos;

    public ReporteClaseDTO() {
    }

    public ReporteClaseDTO(
            int claseId,
            String claseNombre,
            String descripcion,
            LocalDate fecha,
            String estado,
            int trainerId,
            String trainerNombre,
            int cuposTotales,
            int totalInscritos,
            int cuposDisponibles,
            List<ReporteInscritoDTO> inscritos
    ) {
        this.claseId = claseId;
        this.claseNombre = claseNombre;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.estado = estado;
        this.trainerId = trainerId;
        this.trainerNombre = trainerNombre;
        this.cuposTotales = cuposTotales;
        this.totalInscritos = totalInscritos;
        this.cuposDisponibles = cuposDisponibles;
        this.inscritos = inscritos;
    }

    public int getClaseId() {
        return claseId;
    }

    public void setClaseId(int claseId) {
        this.claseId = claseId;
    }

    public String getClaseNombre() {
        return claseNombre;
    }

    public void setClaseNombre(String claseNombre) {
        this.claseNombre = claseNombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerNombre() {
        return trainerNombre;
    }

    public void setTrainerNombre(String trainerNombre) {
        this.trainerNombre = trainerNombre;
    }

    public int getCuposTotales() {
        return cuposTotales;
    }

    public void setCuposTotales(int cuposTotales) {
        this.cuposTotales = cuposTotales;
    }

    public int getTotalInscritos() {
        return totalInscritos;
    }

    public void setTotalInscritos(int totalInscritos) {
        this.totalInscritos = totalInscritos;
    }

    public int getCuposDisponibles() {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(int cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }

    public List<ReporteInscritoDTO> getInscritos() {
        return inscritos;
    }

    public void setInscritos(List<ReporteInscritoDTO> inscritos) {
        this.inscritos = inscritos;
    }
}