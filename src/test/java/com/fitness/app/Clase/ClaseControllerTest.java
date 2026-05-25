package com.fitness.app.Clase;

import com.fitness.app.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    private String generarTokenTrainer() {
        return jwtService.generarToken("trainer@gmail.com");
    }

    private String generarTokenMember() {
        return jwtService.generarToken("member@gmail.com");
    }

    @Test
    void trainerPuedeCrearClase() throws Exception {

        String json = """
        {
            "nombre":"Crossfit",
            "descripcion":"Entrenamiento intenso",
            "trainer":"Lucia",
            "cupos":20
        }
        """;

        mockMvc.perform(post("/api/clases")
                        .header("Authorization",
                                "Bearer " + generarTokenTrainer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void memberNoPuedeCrearClase() throws Exception {

        String json = """
        {
            "nombre":"Yoga",
            "descripcion":"Relajacion",
            "trainer":"Ana",
            "cupos":15
        }
        """;

        mockMvc.perform(post("/api/clases")
                        .header("Authorization",
                                "Bearer " + generarTokenMember())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void listarClasesConToken() throws Exception {

        mockMvc.perform(get("/api/clases")
                        .header("Authorization",
                                "Bearer " + generarTokenTrainer()))
                .andExpect(status().isOk());
    }

    @Test
    void listarClasesSinToken() throws Exception {

        mockMvc.perform(get("/api/clases"))
                .andExpect(status().isForbidden());
    }
}