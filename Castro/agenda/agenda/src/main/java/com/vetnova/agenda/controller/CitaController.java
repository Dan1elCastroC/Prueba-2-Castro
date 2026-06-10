package com.vetnova.agenda.controller;

import com.vetnova.agenda.dto.CitaRequestDTO;
import com.vetnova.agenda.model.Cita;
import com.vetnova.agenda.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/citas") // Unificado a "citas"
@Tag(name = "Citas Médicas", description = "Operaciones para agendar, reprogramar, cancelar y confirmar citas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @PostMapping("/agendar")
    @Operation(summary = "Agendar hora")
    public ResponseEntity<Cita> agendarHora(@Valid @RequestBody CitaRequestDTO dto) {
        Cita cita = new Cita();
        cita.setIdCliente(dto.getIdCliente());
        cita.setIdMascota(dto.getIdMascota());
        cita.setIdVeterinario(dto.getIdVeterinario());
        cita.setFechaHora(dto.getFechaHora());
        cita.setMotivo(dto.getMotivo());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.agendarHora(cita));
    }

    @PutMapping("/{id}/reprogramar")
    @Operation(summary = "Reprogramar cita")
    public ResponseEntity<Cita> reprogramarHora(@PathVariable Long id, @RequestBody Map<String, String> request) {
        LocalDateTime nuevaFecha = LocalDateTime.parse(request.get("nuevaFechaHora"));
        return ResponseEntity.ok(citaService.reprogramarHora(id, nuevaFecha));
    }

    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar cita")
    public ResponseEntity<Cita> cancelarHora(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.cancelarHora(id));
    }

    @PutMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar asistencia")
    public ResponseEntity<Cita> confirmarAsistencia(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.confirmarAsistencia(id));
    }

    @GetMapping
    @Operation(summary = "Listar agenda")
    public ResponseEntity<List<Cita>> listarCitas() {
        return ResponseEntity.ok(citaService.obtenerTodasLasCitas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cita")
    public ResponseEntity<Cita> obtenerCita(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.obtenerCitaPorId(id));
    }
    @GetMapping("/manana")
    @Operation(summary = "Obtener citas de mañana", description = "Endpoint interno consumido por el MS Notificaciones para enviar recordatorios (HU-061)")
    public ResponseEntity<List<Cita>> obtenerCitasDeManana() {
        return ResponseEntity.ok(citaService.obtenerCitasProximas24h());
    }
}