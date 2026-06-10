package com.vetnova.agenda.service;

import com.vetnova.agenda.client.ClienteClient;
import com.vetnova.agenda.client.MascotaClient;
import com.vetnova.agenda.exception.ResourceNotFoundException;
import com.vetnova.agenda.model.Cita;
import com.vetnova.agenda.repository.CitaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class CitaService { // Renombrado a CitaService

    @Autowired
    private CitaRepository citaRepository;
    @Autowired
    private ClienteClient clienteClient;
    @Autowired
    private MascotaClient mascotaClient;

    public Cita agendarHora(Cita cita) {
        log.info("Iniciando agendamiento de hora para mascota ID: {}", cita.getIdMascota());
        
        
        // Como los microservicios 8084 y 8085 de tus compañeros aún no existen,
        // dejamos esto como comentario para evitar el "Connection refused".
        
        if (!clienteClient.validarClienteExiste(cita.getIdCliente())) {
            throw new ResourceNotFoundException("El cliente no existe en el sistema.");
        }
        if (!mascotaClient.validarMascotaExiste(cita.getIdMascota())) {
            throw new ResourceNotFoundException("La mascota no existe en el sistema.");
        }
        
        
        log.info("Simulando validación exitosa de Cliente y Mascota vía Feign Client");

        cita.setEstado("AGENDADA");
        return citaRepository.save(cita);
    }

    public Cita reprogramarHora(Long id, LocalDateTime nuevaFechaHora) {
        log.info("Reprogramando cita ID: {} para la fecha: {}", id, nuevaFechaHora);
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La cita médica con ID " + id + " no fue encontrada."));
        
        cita.setFechaHora(nuevaFechaHora);
        cita.setEstado("REPROGRAMADA");
        return citaRepository.save(cita);
    }

    public Cita cancelarHora(Long id) {
        log.info("Cancelando cita ID: {}", id);
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La cita médica con ID " + id + " no fue encontrada."));
        
        cita.setEstado("CANCELADA");
        return citaRepository.save(cita);
    }

    public Cita confirmarAsistencia(Long id) {
        log.info("Confirmando asistencia del paciente para la cita ID: {}", id);
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La cita médica con ID " + id + " no fue encontrada."));
        
        cita.setEstado("CONFIRMADA");
        return citaRepository.save(cita);
    }
    // Método para leer todas las citas (Consultar agenda)
    public List<Cita> obtenerTodasLasCitas() {
        log.info("Consultando todas las citas médicas agendadas");
        return citaRepository.findAll();
    }

    // Método para leer una cita específica por su ID
    public Cita obtenerCitaPorId(Long id) {
        log.info("Consultando cita médica ID: {}", id);
        return citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La cita médica con ID " + id + " no fue encontrada."));
    }
    // Método para el Microservicio de Notificaciones (HU-061)
    public List<Cita> obtenerCitasProximas24h() {
        log.info("Consultando citas de las próximas 24 horas para envío de recordatorios");
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime manana = ahora.plusDays(1);
        
        // Filtramos las citas que están agendadas para las próximas 24 horas
        return citaRepository.findAll().stream()
                .filter(cita -> cita.getFechaHora().isAfter(ahora) && cita.getFechaHora().isBefore(manana))
                .toList();
    }
}