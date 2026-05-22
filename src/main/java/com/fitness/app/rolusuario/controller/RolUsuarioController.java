package com.fitness.app.rolusuario.controller;

import com.fitness.app.rolusuario.entity.RolUsuario;
import com.fitness.app.rolusuario.service.RolUsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rol-usuario")
public class RolUsuarioController {

    private final RolUsuarioService rolUsuarioService;

    public RolUsuarioController(RolUsuarioService rolUsuarioService) {
        this.rolUsuarioService = rolUsuarioService;
    }

    @PostMapping
    public RolUsuario asignarRol(@RequestBody RolUsuario rolUsuario) {
        return rolUsuarioService.asignarRol(rolUsuario);
    }

    @GetMapping
    public List<RolUsuario> listarRolesUsuario() {
        return rolUsuarioService.listarRolesUsuario();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<RolUsuario> buscarPorUsuario(@PathVariable int idUsuario) {
        return rolUsuarioService.buscarPorUsuario(idUsuario);
    }
}