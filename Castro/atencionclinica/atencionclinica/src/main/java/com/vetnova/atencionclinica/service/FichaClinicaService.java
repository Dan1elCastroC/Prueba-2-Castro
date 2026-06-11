package com.vetnova.atencionclinica.service;

import com.vetnova.atencionclinica.event.EventoDominio;
import com.vetnova.atencionclinica.exception.ResourceNotFoundException;
import com.vetnova.atencionclinica.model.FichaClinica;
import com.vetnova.atencionclinica.repository.FichaClinicaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FichaClinicaService {

    // 1. Inyección por constructor obligatoria (Punto 3 del Mandato)
    private final FichaClinicaRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public FichaClinicaService(FichaClinicaRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public FichaClinica crearFicha(FichaClinica ficha) {
        log.info("Creando ficha clínica para Mascota ID: {}", ficha.getIdMascota());
        ficha.setFechaCreacion(LocalDateTime.now());
        FichaClinica nuevaFicha = repository.save(ficha);
        
        // 2. Emitir evento obligatorio AtencionRegistrada (Punto 4.2 del Mandato)
        Map<String, Object> payload = new HashMap<>();
        payload.put("idFicha", nuevaFicha.getIdFicha());
        payload.put("idMascota", nuevaFicha.getIdMascota());
        payload.put("estado", "REGISTRADA");

        EventoDominio<Map<String, Object>> evento = new EventoDominio<>(
                "AtencionRegistrada",
                "ms-atencion-clinica",
                payload
        );
        eventPublisher.publishEvent(evento);
        log.info("Evento [AtencionRegistrada] emitido exitosamente.");

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