package com.vetnova.atencionclinica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DiagnosticoRequestDTO {

    @NotBlank(message = "La descripción del diagnóstico es obligatoria")
    private String descripcion;

    private String tratamiento;

    private String recetaMedica;

    private String detalleCertificado;

    @NotNull(message = "El ID del veterinario es obligatorio")
    private Long idVeterinario;

    @NotNull(message = "El ID de la ficha clínica es obligatorio")
    private Long idFicha;
}