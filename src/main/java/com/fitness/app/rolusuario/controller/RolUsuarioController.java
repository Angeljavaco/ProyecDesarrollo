package com.fitness.app.rolusuario.controller;

import com.fitness.app.rolusuario.dto.RolUsuarioRequestDTO;
import com.fitness.app.rolusuario.dto.RolUsuarioResponseDTO;
import com.fitness.app.rolusuario.entity.RolUsuario;
import com.fitness.app.rolusuario.service.RolUsuarioService;
import org.springframework.http.ResponseEntity;
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
    public RolUsuarioResponseDTO asignarRol(@RequestBody RolUsuarioRequestDTO dto) {
        RolUsuario rolUsuario = rolUsuarioService.asignarRol(dto);
        return new RolUsuarioResponseDTO(rolUsuario);
    }

    @GetMapping
    public List<RolUsuarioResponseDTO> listarRolesUsuario() {
        return rolUsuarioService.listarRolesUsuario()
                .stream()
                .map(RolUsuarioResponseDTO::new)
                .toList();
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<RolUsuarioResponseDTO> buscarPorUsuario(@PathVariable int usuarioId) {
        return rolUsuarioService.buscarPorUsuario(usuarioId)
                .stream()
                .map(RolUsuarioResponseDTO::new)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRolUsuario(@PathVariable int id) {
        rolUsuarioService.eliminarRolUsuario(id);
        return ResponseEntity.noContent().build();
    }
}