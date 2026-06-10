package com.vetnova.agenda.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaAgendadaEvent {
    private Long idCita;
    private Long idCliente;
    private Long idMascota;
    private LocalDateTime fechaHora;
}