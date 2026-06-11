package com.vetnova.auth.service;

import com.vetnova.auth.dto.LoginRequest;
import com.vetnova.auth.event.EventoDominio;
import com.vetnova.auth.exception.InvalidCredentialsException;
import com.vetnova.auth.model.AuthUsuario;
import com.vetnova.auth.repository.AuthUsuarioRepository;
import com.vetnova.auth.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private AuthUsuarioRepository authUsuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;
    private AuthUsuario usuario;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@vetnova.cl");
        loginRequest.setPassword("admin123");

        usuario = AuthUsuario.builder()
                .id(1L)
                .email("admin@vetnova.cl")
                .password("password_encriptada")
                .rol("ADMIN")
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    @Test
    void testProcesarLogin_Exito() {
        when(authUsuarioRepository.findByEmail("admin@vetnova.cl")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("admin123", "password_encriptada")).thenReturn(true);
        when(jwtUtil.generateToken("admin@vetnova.cl")).thenReturn("token.jwt.simulado");

        String token = authService.procesarLogin(loginRequest);

        assertNotNull(token);
        assertEquals("token.jwt.simulado", token);
        verify(eventPublisher, times(1)).publishEvent(any(EventoDominio.class));
    }

    @Test
    void testProcesarLogin_FalloPorUsuarioNoExiste() {
        when(authUsuarioRepository.findByEmail("admin@vetnova.cl")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.procesarLogin(loginRequest));

        verify(eventPublisher, times(1)).publishEvent(any(EventoDominio.class));
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void testProcesarLogin_FalloPorPasswordIncorrecto() {
        when(authUsuarioRepository.findByEmail("admin@vetnova.cl")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("admin123", "password_encriptada")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.procesarLogin(loginRequest));

        verify(eventPublisher, times(1)).publishEvent(any(EventoDominio.class));
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void testRecuperarContrasena_ExitoYEvento() {
        String mensaje = authService.recuperarContrasena("admin@vetnova.cl");

        assertNotNull(mensaje);
        assertTrue(mensaje.contains("instrucciones"));
        verify(eventPublisher, times(1)).publishEvent(any(EventoDominio.class));
    }
}
