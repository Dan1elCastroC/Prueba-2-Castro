package com.vetnova.auth.config;

import com.vetnova.auth.model.AuthUsuario;
import com.vetnova.auth.repository.AuthUsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner cargarUsuarioInicial(
            AuthUsuarioRepository authUsuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            String emailAdmin = "admin@vetnova.cl";

            if (!authUsuarioRepository.existsByEmail(emailAdmin)) {
                AuthUsuario usuarioAdmin = AuthUsuario.builder()
                        .email(emailAdmin)
                        .password(passwordEncoder.encode("admin123"))
                        .rol("ADMIN")
                        .activo(true)
                        .fechaCreacion(LocalDateTime.now())
                        .build();

                authUsuarioRepository.save(usuarioAdmin);
            }
        };
    }
}
