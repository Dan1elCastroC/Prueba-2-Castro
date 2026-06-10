package com.vetnova.auth.controller;

import com.vetnova.auth.dto.LoginRequest;
import com.vetnova.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    // 1. Variable inmutable (final)
    private final AuthService authService;

    // 2. Inyección mediante el Constructor
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // La validación se hace sola por el @Valid interceptando errores desde el DTO.
        // Si todo está bien, el servicio procesa la lógica y nos devuelve el Token JWT.
        String token = authService.procesarLogin(request);
        
        // Armamos el JSON de respuesta exitosa
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Autenticación exitosa");
        response.put("token", token);

        return ResponseEntity.ok(response); // Devuelve 200 OK
    }

    @PostMapping("/recuperar-password")
    public ResponseEntity<Map<String, String>> recuperarPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String mensaje = authService.recuperarContrasena(email);
        
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", mensaje);
        response.put("status", "success");
        
        return ResponseEntity.ok(response); // Devuelve 200 OK
    }
}