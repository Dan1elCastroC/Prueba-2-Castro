package com.vetnova.atencionclinica.event;

import java.time.LocalDateTime;
import java.util.UUID;

public class RecetaEmitidaEvent {

    private String eventId;
    private String eventType;
    private String source;
    private LocalDateTime occurredAt;
    private Payload payload;

    public RecetaEmitidaEvent(Long idDiagnostico, Long idVeterinario, Long idMascota, String recetaMedica) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = "RecetaEmitida";
        this.source = "ms-atencion-clinica";
        this.occurredAt = LocalDateTime.now();
        this.payload = new Payload(idDiagnostico, idVeterinario, idMascota, recetaMedica);
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getSource() {
        return source;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public Payload getPayload() {
        return payload;
    }

    public static class Payload {
        private Long idDiagnostico;
        private Long idVeterinario;
        private Long idMascota;
        private String recetaMedica;

        public Payload(Long idDiagnostico, Long idVeterinario, Long idMascota, String recetaMedica) {
            this.idDiagnostico = idDiagnostico;
            this.idVeterinario = idVeterinario;
            this.idMascota = idMascota;
            this.recetaMedica = recetaMedica;
        }

        public Long getIdDiagnostico() {
            return idDiagnostico;
        }

        public Long getIdVeterinario() {
            return idVeterinario;
        }

        public Long getIdMascota() {
            return idMascota;
        }

        public String getRecetaMedica() {
            return recetaMedica;
        }
    }
}