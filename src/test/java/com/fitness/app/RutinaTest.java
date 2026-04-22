package com.fitness.app;

import com.fitness.app.model.Rutina;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fitness.app.service.RutinaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RutinaTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private RutinaService rutinaService;

    @BeforeEach
    void setUp() {
        rutinaService.limpiar();
    }

    @Test
    void crearRutina_verificarId() throws Exception {
        Rutina r = new Rutina(10, "Espalda", "Fuerza", 40);

        mockMvc.perform(post("/rutinas")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void crearRutina_validaNombre() throws Exception {
        Rutina r = new Rutina(1, "Piernas", "Fuerza", 45);

        mockMvc.perform(post("/rutinas")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Piernas"));
    }

    @Test
    void crearRutina_validaTipo() throws Exception {
        Rutina r = new Rutina(2, "Brazos", "Fuerza", 30);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Fuerza"));
    }

    @Test
    void crearRutina_validaDuracion() throws Exception {
        Rutina r = new Rutina(3, "Cardio", "Resistencia", 20);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duracion").value(20));
    }

    @Test
    void listar_rutinas_noVacio() throws Exception {
        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(new Rutina(1, "Piernas", "Fuerza", 45))));

        mockMvc.perform(get("/rutinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void listarRutinas_vacio() throws Exception {
        mockMvc.perform(get("/rutinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void actualizar_existente() throws Exception {
        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(new Rutina(1,"Piernas", "Fuerza", 45))));

        Rutina r = new Rutina(1, "Pierna", "Fuerza", 46);

        mockMvc.perform(put("/rutinas/1")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pierna"));
    }

    @Test
    void actualizar_noExiste() throws Exception {

        Rutina r = new Rutina(999, "Test","Fuerza", 10);

        mockMvc.perform(put("/rutinas/999")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(r)))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarRutina_verificarCambio() throws Exception {

        Rutina original = new Rutina(20, "Gluteos", "Fuerza", 30);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(original)));

        Rutina actualizada = new Rutina(20, "GluteosPro", "Fuerza", 60);

        mockMvc.perform(put("/rutinas/20")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(actualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("GluteosPro"))
                .andExpect(jsonPath("$.duracion").value(60));
    }

    @Test
    void eliminar_existe() throws Exception {

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(new Rutina(1,"Piernas","Fuerza",45))));

        mockMvc.perform(delete("/rutinas/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rutinas/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_noExiste() throws Exception {
        mockMvc.perform(delete("/rutinas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarRutina_verificarEliminacion() throws Exception {

        Rutina r = new Rutina(30, "PiernasX", "Fuerza", 50);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)));

        mockMvc.perform(delete("/rutinas/30"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rutinas/30"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarRutina_noExiste() throws Exception {
        mockMvc.perform(get("/rutinas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarRutina_existe() throws Exception {

        Rutina r = new Rutina(1, "Piernas", "Fuerza", 45);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)));

        mockMvc.perform(get("/rutinas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Piernas"));
    }
}