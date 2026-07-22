package com.fitness.app.reporte.controller;

import com.fitness.app.reporte.dto.ReporteClaseDTO;
import com.fitness.app.reporte.service.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(
            ReporteService reporteService
    ) {
        this.reporteService = reporteService;
    }

    @GetMapping("/clases/{claseId}/inscritos")
    public ResponseEntity<ReporteClaseDTO> obtenerReporteInscritos(
            @PathVariable int claseId,
            Authentication authentication
    ) {
        ReporteClaseDTO reporte =
                reporteService.generarReporteInscritos(
                        claseId,
                        authentication.getName()
                );

        return ResponseEntity.ok(reporte);
    }
}