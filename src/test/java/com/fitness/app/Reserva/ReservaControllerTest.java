package com.fitness.app.Reserva;

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
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    private String generarTokenMember() {
        return jwtService.generarToken("member@gmail.com");
    }

    private String generarTokenTrainer() {
        return jwtService.generarToken("trainer@gmail.com");
    }

    @Test
    void memberPuedeReservar() throws Exception {

        String json = """
        {
            "idUsuario":2,
            "idClase":1,
            "fechaReserva":"2026-05-21",
            "estado":"CONFIRMADA"
        }
        """;

        mockMvc.perform(post("/api/reservas")
                        .header("Authorization",
                                "Bearer " + generarTokenMember())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void trainerNoPuedeReservar() throws Exception {

        String json = """
        {
            "idUsuario":2,
            "idClase":1,
            "fechaReserva":"2026-05-21",
            "estado":"CONFIRMADA"
        }
        """;

        mockMvc.perform(post("/api/reservas")
                        .header("Authorization",
                                "Bearer " + generarTokenTrainer())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void listarReservasConToken() throws Exception {

        mockMvc.perform(get("/api/reservas")
                        .header("Authorization",
                                "Bearer " + generarTokenMember()))
                .andExpect(status().isOk());
    }

    @Test
    void listarReservasSinToken() throws Exception {

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isForbidden());
    }
}