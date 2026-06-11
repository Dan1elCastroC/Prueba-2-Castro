package com.vetnova.ventas.service;

import com.vetnova.ventas.event.EventoDominio;
import com.vetnova.ventas.model.Venta;
import com.vetnova.ventas.repository.VentaRepository;
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
public class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private VentaService ventaService;

    private Venta venta;

    @BeforeEach
    void setUp() {
        venta = new Venta();
        venta.setId(1L);
        venta.setIdCliente(10L);
        venta.setIdProducto(100L);
        venta.setCantidad(2);
        venta.setMontoTotal(50000.0);
        venta.setEstado("PENDIENTE");
        venta.setFechaVenta(LocalDateTime.now());
    }

    @Test
    void testRegistrarVenta_Exito() {
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        Venta resultado = ventaService.registrarVenta(venta);

        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
        verify(ventaRepository, times(1)).save(venta);
    }

    @Test
    void testProcesarPago_ExitoYEventos() {
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        Venta resultado = ventaService.procesarPago(1L);

        assertNotNull(resultado);
        assertEquals("PAGADA", resultado.getEstado());
        // El mandato exige 2 eventos al pagar: PagoConfirmado y VentaConfirmada (para el inventario)
        verify(eventPublisher, times(2)).publishEvent(any(EventoDominio.class));
    }

    @Test
    void testRegistrarDevolucion_ExitoYEvento() {
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        Venta resultado = ventaService.registrarDevolucion(1L);

        assertNotNull(resultado);
        assertEquals("DEVUELTA", resultado.getEstado());
        // Verifica que se emitió el evento DevolucionRegistrada
        verify(eventPublisher, times(1)).publishEvent(any(EventoDominio.class));
    }

    @Test
    void testEmitirBoleta_Exito() {
        // Para emitir boleta, la venta DEBE estar pagada
        venta.setEstado("PAGADA");
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        Venta resultado = ventaService.emitirBoleta(1L);

        assertNotNull(resultado);
        assertEquals("PAGADA", resultado.getEstado());
    }

    @Test
    void testEmitirBoleta_FalloPorNoPagada() {
        // Si la venta está PENDIENTE, debe lanzar RuntimeException
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        assertThrows(RuntimeException.class, () -> {
            ventaService.emitirBoleta(1L);
        });
    }
}