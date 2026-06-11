package com.vetnova.auth.service;

import com.vetnova.auth.dto.LoginRequest;
import com.vetnova.auth.event.EventoDominio;
import com.vetnova.auth.exception.InvalidCredentialsException;
import com.vetnova.auth.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

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

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@vetnova.cl");
        loginRequest.setPassword("password123");
    }

    @Test
    void testProcesarLogin_Exito() {
        // Configurar el comportamiento de nuestra "fábrica" de JWT
        when(jwtUtil.generateToken("admin@vetnova.cl")).thenReturn("token.jwt.simulado");

        // Ejecutar el método
        String token = authService.procesarLogin(loginRequest);

        // Verificaciones exigidas por la auditoría
        assertNotNull(token);
        assertEquals("token.jwt.simulado", token);
        
        // El mandato exige verificar que el evento de éxito se disparó
        verify(eventPublisher, times(1)).publishEvent(any(EventoDominio.class));
    }

    @Test
    void testProcesarLogin_FalloPorPasswordInvalido() {
        // Configuramos una contraseña inválida (menor a 6 caracteres como dicta la lógica)
        loginRequest.setPassword("123");

        // Ejecutar y verificar que lanza la excepción correcta
        assertThrows(InvalidCredentialsException.class, () -> {
            authService.procesarLogin(loginRequest);
        });

        // El mandato exige verificar que el evento LoginFallido se publicó para auditoría
        verify(eventPublisher, times(1)).publishEvent(any(EventoDominio.class));
        
        // Aseguramos que por seguridad NO se generó un token
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void testRecuperarContrasena_ExitoYEvento() {
        // Ejecutar el método de recuperación
        String mensaje = authService.recuperarContrasena("admin@vetnova.cl");

        // Verificaciones
        assertNotNull(mensaje);
        assertTrue(mensaje.contains("instrucciones"));
        
        // Verificar que se emitió el evento PasswordRecuperado exigido
        verify(eventPublisher, times(1)).publishEvent(any(EventoDominio.class));
    }
}