package com.vetnova.atencionclinica.client;

import com.vetnova.atencionclinica.dto.MascotaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// Apunta correctamente al puerto 8085 del Microservicio de Mascotas
@FeignClient(name = "mascota-service", url = "http://localhost:8085/api/v1/mascotas")
public interface MascotaClient {

    @GetMapping("/validar/{id}")
    boolean validarMascota(@PathVariable("id") Long id);

    // NUEVO: Método estricto para cumplir el RF11 y HU-030 (Traer datos biológicos)
    @GetMapping("/{id}")
    MascotaDTO obtenerDatosMascota(@PathVariable("id") Long id);
}