package com.vetnova.ventas.service;

import com.vetnova.ventas.client.InventarioClient;
import com.vetnova.ventas.exception.ResourceNotFoundException;
import com.vetnova.ventas.model.Venta;
import com.vetnova.ventas.repository.VentaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private InventarioClient inventarioClient;

    public Venta registrarVenta(Venta venta) {
        log.info("Registrando nueva venta para el cliente ID: {}", venta.getIdCliente());

        // 1. INTEGRACIÓN PREPARADA: Descomentar cuando MS Inventario esté listo
        /*
        if (!inventarioClient.validarStock(venta.getIdProducto(), venta.getCantidad())) {
            log.error("Stock insuficiente para el producto ID: {}", venta.getIdProducto());
            throw new RuntimeException("No hay stock suficiente en el inventario.");
        }
        */
        log.info("Simulando validación de stock exitosa vía Feign Client");

        venta.setEstado("PENDIENTE");
        venta.setFechaVenta(LocalDateTime.now());
        return ventaRepository.save(venta);
    }

    public Venta procesarPago(Long id) {
        log.info("Procesando pago para la venta ID: {}", id);
        Venta venta = obtenerVentaPorId(id);

        // 2. INTEGRACIÓN PREPARADA: Al pagar, se ordena descontar el stock físico (RF15)
        /*
        log.info("Ordenando a Inventario descontar {} unidades del producto {}", venta.getCantidad(), venta.getIdProducto());
        inventarioClient.descontarStock(venta.getIdProducto(), venta.getCantidad());
        */
        log.info("Simulando descuento de stock físico en el MS Inventario vía Feign Client");

        venta.setEstado("PAGADA");
        return ventaRepository.save(venta);
    }

    public Venta registrarDevolucion(Long id) {
        log.info("Registrando devolución para la venta ID: {}", id);
        Venta venta = obtenerVentaPorId(id);
        venta.setEstado("DEVUELTA");
        return ventaRepository.save(venta);
    }

    public Venta emitirBoleta(Long id) {
        log.info("Emitiendo boleta para la venta ID: {}", id);
        Venta venta = obtenerVentaPorId(id);
        if (!venta.getEstado().equals("PAGADA")) {
            throw new RuntimeException("No se puede emitir boleta de una venta que no ha sido pagada.");
        }
        return venta;
    }

    public List<Venta> obtenerTodasLasVentas() {
        log.info("Consultando historial completo de ventas");
        return ventaRepository.findAll();
    }

    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("La venta con ID " + id + " no fue encontrada."));
    }
}