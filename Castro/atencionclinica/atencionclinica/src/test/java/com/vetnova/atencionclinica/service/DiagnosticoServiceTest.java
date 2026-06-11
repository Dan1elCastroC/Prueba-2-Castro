package com.vetnova.atencionclinica.service;

import com.vetnova.atencionclinica.event.CertificadoEmitidoEvent;
import com.vetnova.atencionclinica.event.RecetaEmitidaEvent;
import com.vetnova.atencionclinica.exception.ResourceNotFoundException;
import com.vetnova.atencionclinica.model.Diagnostico;
import com.vetnova.atencionclinica.model.FichaClinica;
import com.vetnova.atencionclinica.repository.DiagnosticoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiagnosticoServiceTest {

    @Mock
    private DiagnosticoRepository repository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private DiagnosticoService service;

    private Diagnostico diagnostico;

    @BeforeEach
    void setUp() {

        FichaClinica ficha = new FichaClinica();
        ficha.setIdFicha(1L);
        ficha.setIdMascota(100L);

        diagnostico = new Diagnostico();
        diagnostico.setIdDiagnostico(1L);
        diagnostico.setIdVeterinario(10L);
        diagnostico.setDescripcion("Otitis");
        diagnostico.setFichaClinica(ficha);
    }

    @Test
    void buscarPorIdDebeRetornarDiagnostico() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(diagnostico));

        Diagnostico resultado =
                service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdDiagnostico());
    }

    @Test
    void buscarPorIdInexistenteDebeLanzarExcepcion() {

        when(repository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.buscarPorId(99L)
        );
    }

    @Test
    void registrarDiagnosticoDebeGuardarCorrectamente() {

        when(repository.save(any(Diagnostico.class)))
                .thenReturn(diagnostico);

        Diagnostico resultado =
                service.registrarDiagnostico(diagnostico);

        assertNotNull(resultado);

        verify(repository)
                .save(any(Diagnostico.class));
    }

    @Test
    void registrarTratamientoDebeActualizarDiagnostico() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(diagnostico));

        when(repository.save(any(Diagnostico.class)))
                .thenReturn(diagnostico);

        Diagnostico resultado =
                service.registrarTratamiento(
                        1L,
                        "Antibiótico por 7 días"
                );

        assertNotNull(resultado);

        verify(repository)
                .save(any(Diagnostico.class));
    }

    @Test
    void emitirRecetaDebePublicarEvento() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(diagnostico));

        when(repository.save(any(Diagnostico.class)))
                .thenReturn(diagnostico);

        service.emitirReceta(
                1L,
                "Amoxicilina 500mg"
        );

        ArgumentCaptor<RecetaEmitidaEvent> captor =
                ArgumentCaptor.forClass(RecetaEmitidaEvent.class);

        verify(eventPublisher)
                .publishEvent(captor.capture());

        assertNotNull(captor.getValue());
    }

    @Test
    void emitirCertificadoDebePublicarEvento() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(diagnostico));

        when(repository.save(any(Diagnostico.class)))
                .thenReturn(diagnostico);

        service.emitirCertificado(
                1L,
                "Reposo por 5 días"
        );

        ArgumentCaptor<CertificadoEmitidoEvent> captor =
                ArgumentCaptor.forClass(CertificadoEmitidoEvent.class);

        verify(eventPublisher)
                .publishEvent(captor.capture());

        assertNotNull(captor.getValue());
    }
}