package com.fitness.app.rol.controller;

import com.fitness.app.rol.entity.Rol;
import com.fitness.app.rol.service.RolService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @PostMapping
    public Rol crearRol(@RequestBody Rol rol) {
        return rolService.crearRol(rol);
    }

    @GetMapping
    public List<Rol> listarRoles() {
        return rolService.listarRoles();
    }
}