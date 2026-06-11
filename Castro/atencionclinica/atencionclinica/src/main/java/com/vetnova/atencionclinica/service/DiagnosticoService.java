package com.vetnova.atencionclinica.service;

import com.vetnova.atencionclinica.event.CertificadoEmitidoEvent;
import com.vetnova.atencionclinica.event.RecetaEmitidaEvent;
import com.vetnova.atencionclinica.exception.ResourceNotFoundException;
import com.vetnova.atencionclinica.model.Diagnostico;
import com.vetnova.atencionclinica.repository.DiagnosticoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiagnosticoService {

    private static final Logger logger = LoggerFactory.getLogger(DiagnosticoService.class);

    private final DiagnosticoRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public DiagnosticoService(DiagnosticoRepository repository,
                              ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional(readOnly = true)
    public Diagnostico buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La atención con ID " + id + " no existe."));
    }

    @Transactional
    public Diagnostico registrarDiagnostico(Diagnostico diagnostico) {
        Diagnostico diagnosticoGuardado = repository.save(diagnostico);

        logger.info("Diagnóstico registrado correctamente. ID: {}",
                diagnosticoGuardado.getIdDiagnostico());

        return diagnosticoGuardado;
    }

    @Transactional
    public Diagnostico registrarTratamiento(Long id, String tratamiento) {
        Diagnostico atencion = buscarPorId(id);
        atencion.setTratamiento(tratamiento);

        Diagnostico actualizado = repository.save(atencion);

        logger.info("Tratamiento registrado para diagnóstico ID: {}",
                actualizado.getIdDiagnostico());

        return actualizado;
    }

    @Transactional
    public Diagnostico emitirReceta(Long id, String receta) {
        Diagnostico atencion = buscarPorId(id);
        atencion.setRecetaMedica(receta);

        Diagnostico actualizado = repository.save(atencion);

        Long idMascota = null;
        if (actualizado.getFichaClinica() != null) {
            idMascota = actualizado.getFichaClinica().getIdMascota();
        }

        RecetaEmitidaEvent evento = new RecetaEmitidaEvent(
                actualizado.getIdDiagnostico(),
                actualizado.getIdVeterinario(),
                idMascota,
                actualizado.getRecetaMedica()
        );

        eventPublisher.publishEvent(evento);

        logger.info("Evento RecetaEmitida publicado. Diagnóstico ID: {}, Mascota ID: {}",
                actualizado.getIdDiagnostico(),
                idMascota);

        return actualizado;
    }

    @Transactional
    public Diagnostico emitirCertificado(Long id, String detalleCertificado) {
        Diagnostico atencion = buscarPorId(id);
        atencion.setDetalleCertificado(detalleCertificado);

        Diagnostico actualizado = repository.save(atencion);

        Long idMascota = null;
        if (actualizado.getFichaClinica() != null) {
            idMascota = actualizado.getFichaClinica().getIdMascota();
        }

        CertificadoEmitidoEvent evento = new CertificadoEmitidoEvent(
                actualizado.getIdDiagnostico(),
                actualizado.getIdVeterinario(),
                idMascota,
                actualizado.getDetalleCertificado()
        );

        eventPublisher.publishEvent(evento);

        logger.info("Evento CertificadoEmitido publicado. Diagnóstico ID: {}, Mascota ID: {}",
                actualizado.getIdDiagnostico(),
                idMascota);

        return actualizado;
    }
}