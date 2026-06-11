package com.vetnova.atencionclinica.controller;

import com.vetnova.atencionclinica.dto.DiagnosticoRequestDTO;
import com.vetnova.atencionclinica.dto.DiagnosticoResponseDTO;
import com.vetnova.atencionclinica.model.Diagnostico;
import com.vetnova.atencionclinica.model.FichaClinica;
import com.vetnova.atencionclinica.service.DiagnosticoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/atenciones")
public class AtencionController {

    private final DiagnosticoService service;

    public AtencionController(DiagnosticoService service) {
        this.service = service;
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DiagnosticoResponseDTO> buscarAtencionPorId(@PathVariable Long id) {
        Diagnostico atencion = service.buscarPorId(id);
        return ResponseEntity.ok(mapToResponse(atencion));
    }

    @PostMapping
    public ResponseEntity<DiagnosticoResponseDTO> registrarDiagnostico(
            @Valid @RequestBody DiagnosticoRequestDTO request) {

        FichaClinica fichaClinica = new FichaClinica();
        fichaClinica.setIdFicha(request.getIdFicha());

        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setDescripcion(request.getDescripcion());
        diagnostico.setTratamiento(request.getTratamiento());
        diagnostico.setRecetaMedica(request.getRecetaMedica());
        diagnostico.setDetalleCertificado(request.getDetalleCertificado());
        diagnostico.setIdVeterinario(request.getIdVeterinario());
        diagnostico.setFichaClinica(fichaClinica);

        Diagnostico nuevaAtencion = service.registrarDiagnostico(diagnostico);

        return new ResponseEntity<>(mapToResponse(nuevaAtencion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/tratamiento")
    public ResponseEntity<DiagnosticoResponseDTO> registrarTratamiento(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        Diagnostico atencion = service.registrarTratamiento(id, request.get("tratamiento"));
        return ResponseEntity.ok(mapToResponse(atencion));
    }

    @PutMapping("/{id}/receta")
    public ResponseEntity<DiagnosticoResponseDTO> emitirReceta(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        Diagnostico atencion = service.emitirReceta(id, request.get("recetaMedica"));
        return ResponseEntity.ok(mapToResponse(atencion));
    }

    @PutMapping("/{id}/certificado")
    public ResponseEntity<DiagnosticoResponseDTO> emitirCertificado(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        Diagnostico atencion = service.emitirCertificado(id, request.get("detalleCertificado"));
        return ResponseEntity.ok(mapToResponse(atencion));
    }

    private DiagnosticoResponseDTO mapToResponse(Diagnostico diagnostico) {
        Long idFicha = null;
        Long idMascota = null;

        if (diagnostico.getFichaClinica() != null) {
            idFicha = diagnostico.getFichaClinica().getIdFicha();
            idMascota = diagnostico.getFichaClinica().getIdMascota();
        }

        return DiagnosticoResponseDTO.builder()
                .idDiagnostico(diagnostico.getIdDiagnostico())
                .descripcion(diagnostico.getDescripcion())
                .tratamiento(diagnostico.getTratamiento())
                .recetaMedica(diagnostico.getRecetaMedica())
                .detalleCertificado(diagnostico.getDetalleCertificado())
                .fecha(diagnostico.getFecha())
                .idVeterinario(diagnostico.getIdVeterinario())
                .idFicha(idFicha)
                .idMascota(idMascota)
                .build();
    }
}