package com.vetnova.atencionclinica.controller;

import com.vetnova.atencionclinica.model.Diagnostico;
import com.vetnova.atencionclinica.service.DiagnosticoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/atenciones")
public class AtencionController {

    private DiagnosticoService service;

    // Buscar una atención
    @GetMapping("/id/{id}")
    public ResponseEntity<Diagnostico> buscarAtencionPorId(@PathVariable Long id) {
        Diagnostico atencion = service.buscarPorId(id);
        return ResponseEntity.ok(atencion);
    }

    // 1. Registrar Diagnóstico (Crear atención directa)
    @PostMapping
    public ResponseEntity<Diagnostico> registrarDiagnostico(@Valid @RequestBody Diagnostico diagnostico) {
        Diagnostico nuevaAtencion = service.registrarDiagnostico(diagnostico);
        return new ResponseEntity<>(nuevaAtencion, HttpStatus.CREATED);
    }

    // 2. Registrar Tratamiento en una atención existente
    @PutMapping("/{id}/tratamiento")
    public ResponseEntity<Diagnostico> registrarTratamiento(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Diagnostico atencion = service.registrarTratamiento(id, request.get("tratamiento"));
        return ResponseEntity.ok(atencion);
    }

    // 3. Emitir Receta Médica
    @PutMapping("/{id}/receta")
    public ResponseEntity<Diagnostico> emitirReceta(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Diagnostico atencion = service.emitirReceta(id, request.get("recetaMedica"));
        return ResponseEntity.ok(atencion);
    }

    // 4. Emitir Certificado Médico
    @PutMapping("/{id}/certificado")
    public ResponseEntity<Diagnostico> emitirCertificado(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Diagnostico atencion = service.emitirCertificado(id, request.get("detalleCertificado"));
        return ResponseEntity.ok(atencion);
    }
}