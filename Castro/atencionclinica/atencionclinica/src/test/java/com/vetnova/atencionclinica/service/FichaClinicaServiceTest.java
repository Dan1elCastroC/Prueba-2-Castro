package com.vetnova.atencionclinica.service;

import com.vetnova.atencionclinica.event.EventoDominio;
import com.vetnova.atencionclinica.exception.ResourceNotFoundException;
import com.vetnova.atencionclinica.model.FichaClinica;
import com.vetnova.atencionclinica.repository.FichaClinicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FichaClinicaServiceTest {

    @Mock
    private FichaClinicaRepository repository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private FichaClinicaService service;

    private FichaClinica ficha;

    @BeforeEach
    void setUp() {
        ficha = new FichaClinica();
        ficha.setIdFicha(1L);
        ficha.setIdMascota(100L);
        ficha.setObservaciones("Control sano general");
        ficha.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void testCrearFicha_ExitoYEvento() {
        // Simulamos que al guardar en BD nos devuelve la ficha
        when(repository.save(any(FichaClinica.class))).thenReturn(ficha);

        FichaClinica resultado = service.crearFicha(ficha);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getIdMascota());
        verify(repository, times(1)).save(ficha);
        
        // El mandato exige verificar que el evento AtencionRegistrada se emita
        verify(eventPublisher, times(1)).publishEvent(any(EventoDominio.class));
    }

    @Test
    void testBuscarPorId_Exito() {
        // Simulamos que encuentra la ficha en BD
        when(repository.findById(1L)).thenReturn(Optional.of(ficha));

        FichaClinica resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdFicha());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorId_FalloLanzaExcepcion() {
        // Simulamos que la BD devuelve vacío
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Verificamos que lance exactamente nuestra ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> {
            service.buscarPorId(99L);
        });
        
        verify(repository, times(1)).findById(99L);
    }

    @Test
    void testObtenerTodas() {
        when(repository.findAll()).thenReturn(List.of(ficha));

        List<FichaClinica> resultado = service.obtenerTodas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(repository, times(1)).findAll();
    }
}