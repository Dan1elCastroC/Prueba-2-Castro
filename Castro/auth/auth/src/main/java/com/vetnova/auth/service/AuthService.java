package com.vetnova.auth.service;

import com.vetnova.auth.dto.LoginRequest;
import com.vetnova.auth.security.JwtUtil;
import com.vetnova.auth.exception.InvalidCredentialsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j // Logs estructurados para auditoría
@Service
public class AuthService {

    @Autowired
    private JwtUtil jwtUtil;

    public String procesarLogin(LoginRequest request) {
        log.info("Iniciando proceso de autenticación para el usuario: {}", request.getEmail());

        log.info("Simulando existencia de usuario vía Feign Client temporalmente");

        // VALIDACIÓN DE CREDENCIALES
        if (!request.getPassword().equals("password123")) {
            log.warn("Fallo de autenticación: Contraseña incorrecta para {}", request.getEmail());
            throw new InvalidCredentialsException("Credenciales inválidas, intente nuevamente");
        }

        // GENERACIÓN DE TOKEN JWT
        String tokenJwt = jwtUtil.generateToken(request.getEmail());
        log.info("Autenticación exitosa. Token generado para el usuario: {}", request.getEmail());
        return tokenJwt;
    }

    public String recuperarContrasena(String email) {
        log.info("Procesando solicitud de recuperación de contraseña para: {}", email);
        return "Si el correo electrónico está registrado, se enviarán las instrucciones para recuperar la contraseña.";
    }
}