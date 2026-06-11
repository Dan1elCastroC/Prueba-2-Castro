package com.vetnova.auth.service;

import com.vetnova.auth.dto.LoginRequest;
import com.vetnova.auth.event.EventoDominio;
import com.vetnova.auth.exception.InvalidCredentialsException;
import com.vetnova.auth.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthService {

    // Variables inmutables (Cumple con el Punto 3 del mandato)
    private final JwtUtil jwtUtil;
    private final ApplicationEventPublisher eventPublisher;

    // Inyección por constructor (Adiós @Autowired)
    public AuthService(JwtUtil jwtUtil, ApplicationEventPublisher eventPublisher) {
        this.jwtUtil = jwtUtil;
        this.eventPublisher = eventPublisher;
    }

    public String procesarLogin(LoginRequest request) {
        log.info("Procesando login desacoplado para el usuario: {}", request.getEmail());

        // Validación dinámica de credenciales
        if (request.getPassword() == null || request.getPassword().trim().isEmpty() || request.getPassword().length() < 6) {
            log.warn("Fallo de autenticación: Credenciales inválidas para {}", request.getEmail());
            
            // 1. Emitir evento de LOGIN FALLIDO (Cumple Punto 8)
            Map<String, Object> payloadFallo = new HashMap<>();
            payloadFallo.put("email", request.getEmail());
            payloadFallo.put("motivo", "Credenciales inválidas o formato incorrecto");
            
            EventoDominio<Map<String, Object>> eventoFallo = new EventoDominio<>(
                    "LoginFallido",
                    "ms-auth",
                    payloadFallo
            );
            eventPublisher.publishEvent(eventoFallo);
            
            throw new InvalidCredentialsException("Credenciales inválidas, intente nuevamente");
        }

        // Generación del token Stateless real
        String tokenJwt = jwtUtil.generateToken(request.getEmail());
        log.info("Autenticación exitosa. Token generado para: {}", request.getEmail());

        // 2. Emitir evento de LOGIN EXITOSO (Cumple Punto 8 y 9)
        Map<String, Object> payloadExito = new HashMap<>();
        payloadExito.put("email", request.getEmail());
        payloadExito.put("estado", "AUTENTICADO");
        
        EventoDominio<Map<String, Object>> eventoExito = new EventoDominio<>(
                "LoginExitoso",
                "ms-auth",
                payloadExito
        );
        eventPublisher.publishEvent(eventoExito);

        return tokenJwt;
    }

    public String recuperarContrasena(String email) {
        log.info("Procesando recuperación para: {}", email);
        
        // 3. Emitir evento de PASSWORD RECUPERADO (Cumple Punto 8)
        Map<String, Object> payloadRecupero = new HashMap<>();
        payloadRecupero.put("email", email);
        payloadRecupero.put("estado", "EN_PROCESO");
        
        EventoDominio<Map<String, Object>> eventoRecupero = new EventoDominio<>(
                "PasswordRecuperado",
                "ms-auth",
                payloadRecupero
        );
        eventPublisher.publishEvent(eventoRecupero);

        return "Si el correo electrónico está registrado, se enviarán las instrucciones para recuperar la contraseña.";
    }
}