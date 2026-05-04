package com.ferremas.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ferremas.backend.model.Usuario;
import com.ferremas.backend.service.UsuarioService;
import com.ferremas.backend.dto.LoginResponse;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin
public class UsuarioController {
  
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PostMapping("/registro")
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestParam String email, @RequestParam String password) {
        return usuarioService.login(email, password);
    }
}
