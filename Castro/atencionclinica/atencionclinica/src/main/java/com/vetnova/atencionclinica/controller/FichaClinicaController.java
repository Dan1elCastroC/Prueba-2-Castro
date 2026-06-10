package com.vetnova.atencionclinica.controller;

import com.vetnova.atencionclinica.model.FichaClinica;
import com.vetnova.atencionclinica.service.FichaClinicaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fichas")
public class FichaClinicaController {

    private FichaClinicaService service;

    @PostMapping
    public ResponseEntity<FichaClinica> crearFicha(@Valid @RequestBody FichaClinica ficha) {
        FichaClinica nuevaFicha = service.crearFicha(ficha);
        return new ResponseEntity<>(nuevaFicha, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FichaClinica>> listarFichas() {
        List<FichaClinica> fichas = service.obtenerTodas();
        if (fichas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(fichas);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<FichaClinica> buscarFichaPorId(@PathVariable Long id) {
        FichaClinica ficha = service.buscarPorId(id);
        return ResponseEntity.ok(ficha); // Devuelve un solo objeto, no una lista
    }
}