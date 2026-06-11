package com.vetnova.auth.repository;

import com.vetnova.auth.model.AuthUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUsuarioRepository extends JpaRepository<AuthUsuario, Long> {

    Optional<AuthUsuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
