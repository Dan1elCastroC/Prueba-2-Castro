package com.vetnova.atencionclinica.repository;

import com.vetnova.atencionclinica.model.FichaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FichaClinicaRepository extends JpaRepository<FichaClinica, Long> {
    Optional<FichaClinica> findByIdMascota(Long idMascota);
}