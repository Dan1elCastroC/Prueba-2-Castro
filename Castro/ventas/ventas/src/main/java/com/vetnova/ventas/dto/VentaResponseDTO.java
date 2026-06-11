package com.vetnova.ventas.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VentaResponseDTO {
    private Long id;
    private Long idCliente;
    private Long idProducto;
    private Integer cantidad;
    private Double montoTotal;
    private String estado;
    private LocalDateTime fechaVenta;
}