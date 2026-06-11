package com.vetnova.atencionclinica.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DiagnosticoResponseDTO {

    private Long idDiagnostico;
    private String descripcion;
    private String tratamiento;
    private String recetaMedica;
    private String detalleCertificado;
    private LocalDateTime fecha;
    private Long idVeterinario;
    private Long idFicha;
    private Long idMascota;
}