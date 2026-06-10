package com.vetnova.ventas.repository;

import com.vetnova.ventas.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}