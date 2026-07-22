package com.fitness.app.usuario.controller;

import com.fitness.app.usuario.dto.UsuarioRequestDTO;
import com.fitness.app.usuario.dto.UsuarioResponseDTO;
import com.fitness.app.usuario.entity.Usuario;
import com.fitness.app.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listarUsuarios()
                .stream()
                .map(UsuarioResponseDTO::new)
                .toList();
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();

        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setTelefono(dto.getTelefono());

        Usuario creado = usuarioService.crearUsuario(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UsuarioResponseDTO(creado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}