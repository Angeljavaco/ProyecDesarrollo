package com.fitness.app;

import com.fitness.app.model.Rutina;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StatsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void total_valido() throws Exception {
        mockMvc.perform(get("/stats/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void total_noNegativo() throws Exception {
        mockMvc.perform(get("/stats/total"))
                .andExpect(jsonPath("$").value(Matchers.greaterThanOrEqualTo(0)));
    }

    @Test
    void total_tipoCorrecto() throws Exception {
        mockMvc.perform(get("/stats/total"))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void stats_total_vacio() throws Exception {
        mockMvc.perform(get("/stats/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(0));
    }

    @Test
    void promedio_valido() throws Exception {
        mockMvc.perform(get("/stats/duracionPromedio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void promedio_rangoValido() throws Exception {
        mockMvc.perform(get("/stats/duracionPromedio"))
                .andExpect(jsonPath("$").value(Matchers.greaterThanOrEqualTo(0)));
    }

    @Test
    void promedio_tipoCorrecto() throws Exception {
        mockMvc.perform(get("/stats/duracionPromedio"))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void stats_promedio_valorEsperado() throws Exception {

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(new Rutina(1, "A", "Fuerza", 40))));

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(new Rutina(2, "B", "Fuerza", 20))));

        mockMvc.perform(get("/stats/duracionPromedio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(30.0));
    }

    @Test
    void max_valido() throws Exception {
        mockMvc.perform(get("/stats/max"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void max_noNegativo() throws Exception {
        mockMvc.perform(get("/stats/max"))
                .andExpect(jsonPath("$").value(Matchers.greaterThanOrEqualTo(0)));
    }

    @Test
    void max_tipoCorrecto() throws Exception {
        mockMvc.perform(get("/stats/max"))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void stats_max_valorCorrecto() throws Exception {

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(new Rutina(1, "A", "Fuerza", 10))));

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(new Rutina(2, "B", "Fuerza", 80))));

        mockMvc.perform(get("/stats/max"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(80));
    }
}