package com.vetnova.auth.controller;

import com.vetnova.auth.dto.LoginRequest;
import com.vetnova.auth.dto.RegistroUsuarioRequest;
import com.vetnova.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.procesarLogin(request);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Autenticación exitosa");
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Map<String, String>> registrarUsuario(@Valid @RequestBody RegistroUsuarioRequest request) {
        String mensaje = authService.registrarUsuario(request);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<Map<String, String>> recuperarPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String mensaje = authService.recuperarContrasena(email);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);
        response.put("status", "success");

        return ResponseEntity.ok(response);
    }
}
