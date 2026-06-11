package com.vetnova.atencionclinica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FichaClinicaRequestDTO {
    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long idMascota;
    
    @NotBlank(message = "Las observaciones no pueden estar vacías")
    private String observaciones;
}