package com.vetnova.ventas.service;

import com.vetnova.ventas.event.EventoDominio;
import com.vetnova.ventas.exception.ResourceNotFoundException;
import com.vetnova.ventas.model.Venta;
import com.vetnova.ventas.repository.VentaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class VentaService {

    // Inyección obligatoria por constructor
    private final VentaRepository ventaRepository;
    private final ApplicationEventPublisher eventPublisher;

    public VentaService(VentaRepository ventaRepository, ApplicationEventPublisher eventPublisher) {
        this.ventaRepository = ventaRepository;
        this.eventPublisher = eventPublisher;
    }

    public Venta registrarVenta(Venta venta) {
        log.info("Registrando nueva venta ID de producto: {}", venta.getIdProducto());
        venta.setEstado("PENDIENTE");
        venta.setFechaVenta(LocalDateTime.now());
        return ventaRepository.save(venta);
    }

    public Venta procesarPago(Long id) {
        Venta venta = obtenerVentaPorId(id);
        venta.setEstado("PAGADA");
        Venta ventaPagada = ventaRepository.save(venta);

        // 1. Emitir evento PagoConfirmado (Exigido por la auditoría)
        Map<String, Object> payloadPago = new HashMap<>();
        payloadPago.put("idVenta", ventaPagada.getId());
        payloadPago.put("monto", ventaPagada.getMontoTotal());
        EventoDominio<Map<String, Object>> eventoPago = new EventoDominio<>(
                "PagoConfirmado", "ms-ventas", payloadPago
        );
        eventPublisher.publishEvent(eventoPago);

        // 2. Emitir evento VentaConfirmada para que Inventario descuente stock (Exigido)
        Map<String, Object> payloadVenta = new HashMap<>();
        payloadVenta.put("idVenta", ventaPagada.getId());
        payloadVenta.put("idProducto", ventaPagada.getIdProducto());
        payloadVenta.put("cantidad", ventaPagada.getCantidad());
        EventoDominio<Map<String, Object>> eventoVenta = new EventoDominio<>(
                "VentaConfirmada", "ms-ventas", payloadVenta
        );
        eventPublisher.publishEvent(eventoVenta);

        log.info("Eventos [PagoConfirmado] y [VentaConfirmada] emitidos exitosamente.");
        return ventaPagada;
    }

    public Venta registrarDevolucion(Long id) {
        Venta venta = obtenerVentaPorId(id);
        venta.setEstado("DEVUELTA");
        Venta ventaDevuelta = ventaRepository.save(venta);

        // 3. Emitir evento DevolucionRegistrada para retornar stock a Inventario (Exigido)
        Map<String, Object> payloadDev = new HashMap<>();
        payloadDev.put("idVenta", ventaDevuelta.getId());
        payloadDev.put("idProducto", ventaDevuelta.getIdProducto());
        payloadDev.put("cantidad", ventaDevuelta.getCantidad());
        EventoDominio<Map<String, Object>> eventoDev = new EventoDominio<>(
                "DevolucionRegistrada", "ms-ventas", payloadDev
        );
        eventPublisher.publishEvent(eventoDev);

        log.info("Evento [DevolucionRegistrada] emitido exitosamente.");
        return ventaDevuelta;
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