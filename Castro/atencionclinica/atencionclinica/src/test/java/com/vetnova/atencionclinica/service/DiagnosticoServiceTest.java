package com.vetnova.atencionclinica.service;

import com.vetnova.atencionclinica.model.Diagnostico;
import com.vetnova.atencionclinica.repository.DiagnosticoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DiagnosticoServiceTest {

    @Mock
    private DiagnosticoRepository diagnosticoRepository;

    @InjectMocks
    private DiagnosticoService diagnosticoService;

    private Diagnostico diagnostico;

    @BeforeEach
    void setUp() {
        diagnostico = new Diagnostico();
        diagnostico.setIdDiagnostico(1L);
        diagnostico.setDescripcion("Otitis leve en el oído derecho");
        diagnostico.setFecha(LocalDateTime.now());
        diagnostico.setIdVeterinario(5L);
    }

    @Test
    void testRegistrarDiagnostico_Exito() {
        // Simulamos el guardado en la base de datos
        when(diagnosticoRepository.save(any(Diagnostico.class))).thenReturn(diagnostico);

        Diagnostico resultado = diagnosticoService.registrarDiagnostico(diagnostico);

        // Verificamos que se cree correctamente según la regla de negocio
        assertNotNull(resultado);
        assertEquals("Otitis leve en el oído derecho", resultado.getDescripcion());
        assertEquals(5L, resultado.getIdVeterinario());
        
        verify(diagnosticoRepository, times(1)).save(diagnostico);
    }
}