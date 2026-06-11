package com.vetnova.atencionclinica.service;

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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FichaClinicaServiceTest {

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
    }

    @Test
    void crearFichaDebeGuardarCorrectamente() {

        when(repository.save(any(FichaClinica.class)))
                .thenReturn(ficha);

        FichaClinica resultado = service.crearFicha(ficha);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdFicha());

        verify(repository, times(1))
                .save(any(FichaClinica.class));
    }

    @Test
    void obtenerTodasDebeRetornarLista() {

        when(repository.findAll())
                .thenReturn(List.of(ficha));

        List<FichaClinica> resultado =
                service.obtenerTodas();

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorIdDebeRetornarFicha() {

        when(repository.findById(1L))
                .thenReturn(Optional.of(ficha));

        FichaClinica resultado =
                service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getIdMascota());
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
}