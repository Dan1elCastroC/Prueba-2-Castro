package com.vetnova.ventas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

// Apunta al controlador de Productos del MS Inventario de Pamela (Puerto 8087)
@FeignClient(name = "inventario-service", url = "http://localhost:8087/api/v1/productos")
public interface InventarioClient {

    @GetMapping("/validar-stock/{idProducto}/{cantidad}")
    boolean validarStock(@PathVariable("idProducto") Long idProducto, @PathVariable("cantidad") Integer cantidad);

    // NUEVO: Método para ordenar el descuento de stock tras pagar
    @PutMapping("/descontar-stock/{idProducto}/{cantidad}")
    void descontarStock(@PathVariable("idProducto") Long idProducto, @PathVariable("cantidad") Integer cantidad);
}