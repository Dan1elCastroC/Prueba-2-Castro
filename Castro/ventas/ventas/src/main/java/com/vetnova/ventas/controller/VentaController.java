package com.vetnova.ventas.controller;

import com.vetnova.ventas.dto.VentaRequestDTO;
import com.vetnova.ventas.model.Venta;
import com.vetnova.ventas.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    // 1. Variable inmutable (final)
    private final VentaService ventaService;

    // 2. Inyección mediante el Constructor
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    // 1. Registrar Venta
    @PostMapping("/registrar")
    public ResponseEntity<Venta> registrarVenta(@Valid @RequestBody VentaRequestDTO dto) {
        Venta venta = new Venta();
        venta.setIdCliente(dto.getIdCliente());
        venta.setIdProducto(dto.getIdProducto());
        venta.setCantidad(dto.getCantidad());
        venta.setMontoTotal(dto.getMontoTotal());

        Venta nuevaVenta = ventaService.registrarVenta(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
    }

    // 2. Procesar Pago
    @PutMapping("/{id}/pagar")
    public ResponseEntity<Venta> procesarPago(@PathVariable Long id) {
        Venta ventaPagada = ventaService.procesarPago(id);
        return ResponseEntity.ok(ventaPagada);
    }

    // 3. Registrar Devolución
    @PutMapping("/{id}/devolucion")
    public ResponseEntity<Venta> registrarDevolucion(@PathVariable Long id) {
        Venta ventaDevuelta = ventaService.registrarDevolucion(id);
        return ResponseEntity.ok(ventaDevuelta);
    }

    // 4. Emitir Boleta
    @GetMapping("/{id}/boleta")
    public ResponseEntity<Venta> emitirBoleta(@PathVariable Long id) {
        Venta boleta = ventaService.emitirBoleta(id);
        return ResponseEntity.ok(boleta);
    }

    // 5. Historial de compras / ventas
    @GetMapping
    public ResponseEntity<List<Venta>> listarVentas() {
        return ResponseEntity.ok(ventaService.obtenerTodasLasVentas());
    }

    // 6. Consultar venta específica
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVenta(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.obtenerVentaPorId(id));
    }
}