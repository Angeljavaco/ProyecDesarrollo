package com.fitness.app;

import com.fitness.app.model.Rutina;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RutinaTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    // ========================
    // 🟢 POST (CREAR)
    // ========================

    @Test
    public void testGuardar1() throws Exception {
        Rutina r = new Rutina(1, "Piernas", "Fuerza", 45);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Piernas"));
    }

    @Test
    public void testGuardar2() throws Exception {
        Rutina r = new Rutina(2, "Brazos", "Fuerza", 30);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Fuerza"));
    }

    @Test
    public void testGuardar3() throws Exception {
        Rutina r = new Rutina(3, "Cardio", "Resistencia", 20);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duracion").value(20));
    }

    // ========================
    // 🟢 GET (LISTAR)
    // ========================

    @Test
    public void testListar1() throws Exception {
        mockMvc.perform(get("/rutinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testListar2() throws Exception {
        mockMvc.perform(get("/rutinas"))
                .andExpect(status().isOk());
    }

    @Test
    public void testListar3() throws Exception {
        mockMvc.perform(get("/rutinas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    // ========================
    // 🟢 GET BY ID
    // ========================

    @Test
    public void testBuscar1() throws Exception {

        // 🔥 Crear primero
        Rutina r = new Rutina(1, "Piernas", "Fuerza", 45);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)));

        // 🔥 Buscar
        mockMvc.perform(get("/rutinas/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testBuscar2() throws Exception {

        Rutina r = new Rutina(2, "Brazos", "Fuerza", 30);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)));

        mockMvc.perform(get("/rutinas/2"))
                .andExpect(status().isOk());
    }

    @Test
    public void testBuscar3() throws Exception {
        mockMvc.perform(get("/rutinas/999"))
                .andExpect(status().isNotFound());
    }

    // ========================
    // 🟢 PUT (ACTUALIZAR)
    // ========================

    @Test
    public void testActualizar1() throws Exception {

        // 🔥 Crear primero
        Rutina r = new Rutina(1, "Piernas", "Fuerza", 45);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)));

        // 🔥 Actualizar
        Rutina nueva = new Rutina(1, "PiernasPro", "Fuerza", 50);

        mockMvc.perform(put("/rutinas/1")
                .contentType("application/json")
                .content(mapper.writeValueAsString(nueva)))
                .andExpect(status().isOk());
    }

    @Test
    public void testActualizar2() throws Exception {

        Rutina r = new Rutina(2, "Brazos", "Fuerza", 30);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)));

        Rutina nueva = new Rutina(2, "BrazosPro", "Fuerza", 35);

        mockMvc.perform(put("/rutinas/2")
                .contentType("application/json")
                .content(mapper.writeValueAsString(nueva)))
                .andExpect(status().isOk());
    }

    @Test
    public void testActualizar3() throws Exception {

        Rutina nueva = new Rutina(999, "X", "Y", 10);

        mockMvc.perform(put("/rutinas/999")
                .contentType("application/json")
                .content(mapper.writeValueAsString(nueva)))
                .andExpect(status().isNotFound());
    }

    // ========================
    // 🟢 DELETE
    // ========================

    @Test
    public void testEliminar1() throws Exception {

        Rutina r = new Rutina(1, "Piernas", "Fuerza", 45);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)));

        mockMvc.perform(delete("/rutinas/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testEliminar2() throws Exception {

        Rutina r = new Rutina(2, "Brazos", "Fuerza", 30);

        mockMvc.perform(post("/rutinas")
                .contentType("application/json")
                .content(mapper.writeValueAsString(r)));

        mockMvc.perform(delete("/rutinas/2"))
                .andExpect(status().isOk());
    }

    @Test
    public void testEliminar3() throws Exception {
        mockMvc.perform(delete("/rutinas/999"))
                .andExpect(status().isNotFound());
    }

    // ========================
    // 🟢 FILTROS
    // ========================

    @Test
    public void testFiltroTipo1() throws Exception {
        mockMvc.perform(get("/rutinas/tipo/Fuerza"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testFiltroTipo2() throws Exception {
        mockMvc.perform(get("/rutinas/tipo/Resistencia"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFiltroTipo3() throws Exception {
        mockMvc.perform(get("/rutinas/tipo/X"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFiltroDuracion1() throws Exception {
        mockMvc.perform(get("/rutinas/duracion/30"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFiltroDuracion2() throws Exception {
        mockMvc.perform(get("/rutinas/duracion/10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFiltroDuracion3() throws Exception {
        mockMvc.perform(get("/rutinas/duracion/100"))
                .andExpect(status().isOk());
    }

    // ========================
    // 🟢 COUNT
    // ========================

    @Test
    public void testCount1() throws Exception {
        mockMvc.perform(get("/rutinas/count"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCount2() throws Exception {
        mockMvc.perform(get("/rutinas/count"))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void testCount3() throws Exception {
        mockMvc.perform(get("/rutinas/count"))
                .andExpect(status().isOk());
    }
}