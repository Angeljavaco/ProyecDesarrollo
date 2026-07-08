package com.fitness.app.tests.usuario;

import com.fitness.app.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    private String generarTokenAdmin() {
        return jwtService.generarToken("angel@gmail.com");
    }

    @Test
    void crearUsuarioCorrecto() throws Exception {
        String email = "test" + System.currentTimeMillis() + "@gmail.com";
        String json = """
        {
            "nombre":"Nuevo",
            "email":"%s",
            "password":"123456",
            "telefono":"999999999"
        }
        """.formatted(email);

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void crearUsuarioSinEmail() throws Exception {
        String json = """
        {
            "nombre":"Nuevo",
            "password":"123456",
            "telefono":"999999999"
        }
        """;

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarUsuariosConToken() throws Exception {

        mockMvc.perform(get("/api/usuarios")
                        .header("Authorization",
                                "Bearer " + generarTokenAdmin()))
                .andExpect(status().isOk());
    }

    @Test
    void listarUsuariosSinToken() throws Exception {

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isForbidden());
    }
}
