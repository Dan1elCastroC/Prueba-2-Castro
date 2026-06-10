package com.vetnova.ventas.service;

import com.vetnova.ventas.event.VentaConfirmadaEvent;
import com.vetnova.ventas.exception.ResourceNotFoundException;
import com.vetnova.ventas.model.Venta;
import com.vetnova.ventas.repository.VentaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class VentaService {

    // 1. Las variables ahora son 'final' (inmutables)
    private final VentaRepository ventaRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 2. Inyección de dependencias mediante el Constructor
    public VentaService(VentaRepository ventaRepository, ApplicationEventPublisher eventPublisher) {
        this.ventaRepository = ventaRepository;
        this.eventPublisher = eventPublisher;
    }

    public Venta registrarVenta(Venta venta) {
        log.info("Registrando venta de forma autónoma. Stock se validará asíncronamente.");
        venta.setEstado("PENDIENTE");
        venta.setFechaVenta(LocalDateTime.now());
        return ventaRepository.save(venta);
    }

    public Venta procesarPago(Long id) {
        Venta venta = obtenerVentaPorId(id);
        venta.setEstado("PAGADA");
        Venta ventaPagada = ventaRepository.save(venta);

        // Disparamos el EVENTO para que Inventario descuente el stock
        VentaConfirmadaEvent evento = new VentaConfirmadaEvent(
                ventaPagada.getId(), 
                ventaPagada.getIdProducto(), 
                ventaPagada.getCantidad()
        );
        eventPublisher.publishEvent(evento);
        log.info("Evento [VentaConfirmadaEvent] emitido. Inventario descontará el stock de forma asíncrona.");

        return ventaPagada;
    }

    public Venta registrarDevolucion(Long id) {
        Venta venta = obtenerVentaPorId(id);
        venta.setEstado("DEVUELTA");
        return ventaRepository.save(venta);
    }

    public Venta emitirBoleta(Long id) {
        Venta venta = obtenerVentaPorId(id);
        if (!venta.getEstado().equals("PAGADA")) {
            throw new RuntimeException("Solo se emiten boletas de ventas pagadas.");
        }
        return venta;
    }

    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + id));
    }
}