package com.vetnova.atencionclinica.service;

import com.vetnova.atencionclinica.exception.ResourceNotFoundException;
import com.vetnova.atencionclinica.model.Diagnostico;
import com.vetnova.atencionclinica.repository.DiagnosticoRepository;
import org.springframework.stereotype.Service;

@Service
public class DiagnosticoService {


    private DiagnosticoRepository repository;

    public Diagnostico buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La atención con ID " + id + " no existe."));
    }

    // 1. Registrar Diagnóstico (Crear la atención)
    public Diagnostico registrarDiagnostico(Diagnostico diagnostico) {
        return repository.save(diagnostico);
    }

    // 2. Registrar Tratamiento
    public Diagnostico registrarTratamiento(Long id, String tratamiento) {
        Diagnostico atencion = buscarPorId(id);
        atencion.setTratamiento(tratamiento);
        return repository.save(atencion);
    }

    // 3. Emitir Receta
    public Diagnostico emitirReceta(Long id, String receta) {
        Diagnostico atencion = buscarPorId(id);
        atencion.setRecetaMedica(receta);
        return repository.save(atencion);
    }

    // 4. Emitir Certificado
    public Diagnostico emitirCertificado(Long id, String detalleCertificado) {
        Diagnostico atencion = buscarPorId(id);
        atencion.setDetalleCertificado(detalleCertificado);
        return repository.save(atencion);
    }
}