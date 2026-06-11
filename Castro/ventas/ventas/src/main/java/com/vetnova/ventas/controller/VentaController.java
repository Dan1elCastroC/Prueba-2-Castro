package com.vetnova.ventas.controller;

import com.vetnova.ventas.dto.VentaRequestDTO;
import com.vetnova.ventas.dto.VentaResponseDTO;
import com.vetnova.ventas.model.Venta;
import com.vetnova.ventas.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping("/registrar")
    public ResponseEntity<VentaResponseDTO> registrarVenta(@Valid @RequestBody VentaRequestDTO dto) {
        Venta venta = new Venta();
        venta.setIdCliente(dto.getIdCliente());
        venta.setIdProducto(dto.getIdProducto());
        venta.setCantidad(dto.getCantidad());
        venta.setMontoTotal(dto.getMontoTotal());

        Venta nuevaVenta = ventaService.registrarVenta(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapearADTO(nuevaVenta));
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<VentaResponseDTO> procesarPago(@PathVariable Long id) {
        Venta ventaPagada = ventaService.procesarPago(id);
        return ResponseEntity.ok(mapearADTO(ventaPagada));
    }

    @PutMapping("/{id}/devolucion")
    public ResponseEntity<VentaResponseDTO> registrarDevolucion(@PathVariable Long id) {
        Venta ventaDevuelta = ventaService.registrarDevolucion(id);
        return ResponseEntity.ok(mapearADTO(ventaDevuelta));
    }

    @GetMapping("/{id}/boleta")
    public ResponseEntity<VentaResponseDTO> emitirBoleta(@PathVariable Long id) {
        Venta boleta = ventaService.emitirBoleta(id);
        return ResponseEntity.ok(mapearADTO(boleta));
    }

    @GetMapping
    public ResponseEntity<List<VentaResponseDTO>> listarVentas() {
        List<VentaResponseDTO> ventas = ventaService.obtenerTodasLasVentas().stream()
                .map(this::mapearADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ventas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponseDTO> obtenerVenta(@PathVariable Long id) {
        return ResponseEntity.ok(mapearADTO(ventaService.obtenerVentaPorId(id)));
    }

    // Método interno para mapear Entidad a DTO (Cumple Punto 10)
    private VentaResponseDTO mapearADTO(Venta venta) {
        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setId(venta.getId());
        dto.setIdCliente(venta.getIdCliente());
        dto.setIdProducto(venta.getIdProducto());
        dto.setCantidad(venta.getCantidad());
        dto.setMontoTotal(venta.getMontoTotal());
        dto.setEstado(venta.getEstado());
        dto.setFechaVenta(venta.getFechaVenta());
        return dto;
    }
}