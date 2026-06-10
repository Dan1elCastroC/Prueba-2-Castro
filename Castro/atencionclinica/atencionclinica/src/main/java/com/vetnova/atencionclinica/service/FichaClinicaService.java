package com.vetnova.atencionclinica.service;

import com.vetnova.atencionclinica.exception.ResourceNotFoundException;
import com.vetnova.atencionclinica.model.FichaClinica;
import com.vetnova.atencionclinica.repository.FichaClinicaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class FichaClinicaService {

    private FichaClinicaRepository repository;

    @Transactional
    public FichaClinica crearFicha(FichaClinica ficha) {
        log.info("Creando ficha clínica para Mascota ID: {} (Arquitectura Desacoplada)", ficha.getIdMascota());
        
        if (ficha.getDiagnosticos() != null) {
            ficha.getDiagnosticos().forEach(diag -> diag.setFichaClinica(ficha));
        }

        FichaClinica nuevaFicha = repository.save(ficha);
        log.info("Ficha creada exitosamente con ID: {}", nuevaFicha.getIdFicha());
        return nuevaFicha;
    }

    public List<FichaClinica> obtenerTodas() {
        return repository.findAll();
    }

    public FichaClinica buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La ficha clínica con ID " + id + " no existe."));
    }
}