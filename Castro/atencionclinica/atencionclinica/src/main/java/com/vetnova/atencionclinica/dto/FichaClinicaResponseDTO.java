package com.vetnova.atencionclinica.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FichaClinicaResponseDTO {
    private Long idFicha;
    private Long idMascota;
    private LocalDateTime fechaCreacion;
    private String observaciones;
}