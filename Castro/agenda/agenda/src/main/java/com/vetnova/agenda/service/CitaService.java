package com.vetnova.agenda.service;

import com.vetnova.agenda.event.EventoDominio;
import com.vetnova.agenda.exception.ResourceNotFoundException;
import com.vetnova.agenda.model.Cita;
import com.vetnova.agenda.repository.CitaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class CitaService {

    // 1. Las variables ahora son 'final' (inmutables)
    private final CitaRepository citaRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 2. Inyección de dependencias mediante el Constructor (Adiós @Autowired)
    public CitaService(CitaRepository citaRepository, ApplicationEventPublisher eventPublisher) {
        this.citaRepository = citaRepository;
        this.eventPublisher = eventPublisher;
    }

    public Cita agendarHora(Cita cita) {
        log.info("Iniciando registro asíncrono de cita. Validaciones delegadas a la consistencia eventual.");
        
        cita.setEstado("AGENDADA");
        Cita nuevaCita = citaRepository.save(cita);
        log.info("Cita guardada en BD local con ID: {}", nuevaCita.getId());

        // 1. Armamos el Payload exactamente como pide el Mandato (Punto 9)
        Map<String, Object> payload = new HashMap<>();
        payload.put("idCita", nuevaCita.getId());
        payload.put("idCliente", nuevaCita.getIdCliente());
        payload.put("idMascota", nuevaCita.getIdMascota());
        payload.put("idSucursal", 3); // Valor de ejemplo o sacado de la cita
        payload.put("fecha", nuevaCita.getFechaHora().toLocalDate().toString());
        payload.put("hora", nuevaCita.getFechaHora().toLocalTime().toString());
        payload.put("estado", "AGENDADA");

        // 2. Envolvemos el payload en el EventoDominio estándar
        EventoDominio<Map<String, Object>> evento = new EventoDominio<>(
                "CitaAgendada",
                "ms-agenda",
                payload
        );
        
        // 3. Publicamos el evento
        eventPublisher.publishEvent(evento);
        log.info("Evento estandarizado [CitaAgendada] emitido con EventID: {}", evento.getEventId());

        return nuevaCita;
    }

    public Cita reprogramarHora(Long id, LocalDateTime nuevaFecha) {
        Cita cita = obtenerCitaPorId(id);
        cita.setFechaHora(nuevaFecha);
        return citaRepository.save(cita);
    }

    public Cita cancelarHora(Long id) {
        Cita cita = obtenerCitaPorId(id);
        cita.setEstado("CANCELADA");
        return citaRepository.save(cita);
    }

    public Cita confirmarAsistencia(Long id) {
        Cita cita = obtenerCitaPorId(id);
        cita.setEstado("CONFIRMADA");
        return citaRepository.save(cita);
    }

    public List<Cita> obtenerTodasLasCitas() {
        return citaRepository.findAll();
    }

    public Cita obtenerCitaPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));
    }

    public List<Cita> obtenerCitasProximas24h() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime manana = ahora.plusDays(1);
        return citaRepository.findAll().stream()
                .filter(cita -> cita.getFechaHora().isAfter(ahora) && cita.getFechaHora().isBefore(manana))
                .toList();
    }
}