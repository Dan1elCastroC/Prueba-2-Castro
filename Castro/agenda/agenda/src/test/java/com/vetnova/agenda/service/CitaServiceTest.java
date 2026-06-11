package com.vetnova.agenda.service;

import com.vetnova.agenda.event.EventoDominio;
import com.vetnova.agenda.exception.ResourceNotFoundException;
import com.vetnova.agenda.model.Cita;
import com.vetnova.agenda.repository.CitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private CitaService citaService;

    private Cita cita;

    @BeforeEach
    void setUp() {
        cita = new Cita();
        cita.setId(1L);
        cita.setIdCliente(10L);
        cita.setIdMascota(20L);
        cita.setFechaHora(LocalDateTime.now().plusDays(1));
        cita.setEstado("PENDIENTE");
    }

    @Test
    void testAgendarHora_CrearYEmitirEvento() {
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.agendarHora(cita);

        assertNotNull(resultado);
        assertEquals("AGENDADA", resultado.getEstado());
        verify(citaRepository, times(1)).save(cita);
        verify(eventPublisher, times(1)).publishEvent(any(EventoDominio.class));
    }

    @Test
    void testObtenerCitaPorId_BuscarExitoso() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        Cita resultado = citaService.obtenerCitaPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(citaRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerCitaPorId_ValidacionExcepcion() {
        when(citaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            citaService.obtenerCitaPorId(99L);
        });
        verify(citaRepository, times(1)).findById(99L);
    }

    @Test
    void testReprogramarHora_Actualizar() {
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(5);
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.reprogramarHora(1L, nuevaFecha);

        assertNotNull(resultado);
        assertEquals(nuevaFecha, resultado.getFechaHora());
        verify(citaRepository, times(1)).findById(1L);
        verify(citaRepository, times(1)).save(cita);
    }

    @Test
    void testCancelarHora_Anular() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        Cita resultado = citaService.cancelarHora(1L);

        assertNotNull(resultado);
        assertEquals("CANCELADA", resultado.getEstado());
        verify(citaRepository, times(1)).save(cita);
    }
}