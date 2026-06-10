package com.vetnova.ventas.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaConfirmadaEvent {
    private Long idVenta;
    private Long idProducto;
    private Integer cantidad;
}