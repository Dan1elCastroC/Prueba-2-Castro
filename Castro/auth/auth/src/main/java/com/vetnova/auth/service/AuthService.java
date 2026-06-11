package com.vetnova.auth.service;

import com.vetnova.auth.dto.LoginRequest;
import com.vetnova.auth.dto.RegistroUsuarioRequest;
import com.vetnova.auth.event.EventoDominio;
import com.vetnova.auth.exception.InvalidCredentialsException;
import com.vetnova.auth.model.AuthUsuario;
import com.vetnova.auth.repository.AuthUsuarioRepository;
import com.vetnova.auth.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthUsuarioRepository authUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            JwtUtil jwtUtil,
            ApplicationEventPublisher eventPublisher,
            AuthUsuarioRepository authUsuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.jwtUtil = jwtUtil;
        this.eventPublisher = eventPublisher;
        this.authUsuarioRepository = authUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String procesarLogin(LoginRequest request) {
        log.info("Procesando login con base de datos para el usuario: {}", request.getEmail());

        AuthUsuario usuario = authUsuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    publicarEventoLoginFallido(request.getEmail(), "Usuario no registrado");
                    return new InvalidCredentialsException("Credenciales inválidas, intente nuevamente");
                });

        if (!usuario.isActivo()) {
            publicarEventoLoginFallido(request.getEmail(), "Usuario inactivo");
            throw new InvalidCredentialsException("Usuario inactivo, contacte al administrador");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            publicarEventoLoginFallido(request.getEmail(), "Contraseña incorrecta");
            throw new InvalidCredentialsException("Credenciales inválidas, intente nuevamente");
        }

        String tokenJwt = jwtUtil.generateToken(usuario.getEmail());
        log.info("Autenticación exitosa. Token generado para: {}", usuario.getEmail());

        Map<String, Object> payloadExito = new HashMap<>();
        payloadExito.put("email", usuario.getEmail());
        payloadExito.put("rol", usuario.getRol());
        payloadExito.put("estado", "AUTENTICADO");

        EventoDominio<Map<String, Object>> eventoExito = new EventoDominio<>(
                "LoginExitoso",
                "ms-auth",
                payloadExito
        );
        eventPublisher.publishEvent(eventoExito);

        return tokenJwt;
    }

    public String registrarUsuario(RegistroUsuarioRequest request) {
        log.info("Registrando usuario en Auth: {}", request.getEmail());

        if (authUsuarioRepository.existsByEmail(request.getEmail())) {
            throw new InvalidCredentialsException("Ya existe un usuario registrado con ese correo");
        }

        AuthUsuario usuario = AuthUsuario.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol().toUpperCase())
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        authUsuarioRepository.save(usuario);

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", usuario.getEmail());
        payload.put("rol", usuario.getRol());
        payload.put("estado", "CREADO");

        EventoDominio<Map<String, Object>> evento = new EventoDominio<>(
                "UsuarioAuthCreado",
                "ms-auth",
                payload
        );
        eventPublisher.publishEvent(evento);

        return "Usuario registrado correctamente";
    }

    public String recuperarContrasena(String email) {
        log.info("Procesando recuperación para: {}", email);

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

    private void publicarEventoLoginFallido(String email, String motivo) {
        log.warn("Fallo de autenticación para {}. Motivo: {}", email, motivo);

        Map<String, Object> payloadFallo = new HashMap<>();
        payloadFallo.put("email", email);
        payloadFallo.put("motivo", motivo);

        EventoDominio<Map<String, Object>> eventoFallo = new EventoDominio<>(
                "LoginFallido",
                "ms-auth",
                payloadFallo
        );
        eventPublisher.publishEvent(eventoFallo);
    }
}
