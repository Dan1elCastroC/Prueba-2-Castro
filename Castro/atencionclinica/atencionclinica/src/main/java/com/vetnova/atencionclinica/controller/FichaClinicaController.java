package com.vetnova.atencionclinica.controller;

import com.vetnova.atencionclinica.dto.FichaClinicaRequestDTO;
import com.vetnova.atencionclinica.dto.FichaClinicaResponseDTO;
import com.vetnova.atencionclinica.model.FichaClinica;
import com.vetnova.atencionclinica.service.FichaClinicaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fichas")
public class FichaClinicaController {

    private final FichaClinicaService service;

    public FichaClinicaController(FichaClinicaService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FichaClinicaResponseDTO> crearFicha(@Valid @RequestBody FichaClinicaRequestDTO request) {
        // Mapeo manual de DTO a Entidad
        FichaClinica ficha = new FichaClinica();
        ficha.setIdMascota(request.getIdMascota());
        ficha.setObservaciones(request.getObservaciones());

        FichaClinica fichaCreada = service.crearFicha(ficha);

        // Mapeo de Entidad a ResponseDTO (Punto 10 del mandato)
        FichaClinicaResponseDTO response = new FichaClinicaResponseDTO();
        response.setIdFicha(fichaCreada.getIdFicha());
        response.setIdMascota(fichaCreada.getIdMascota());
        response.setFechaCreacion(fichaCreada.getFechaCreacion());
        response.setObservaciones(fichaCreada.getObservaciones());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}