package com.vetnova.atencionclinica.event;

import java.time.LocalDateTime;
import java.util.UUID;

public class CertificadoEmitidoEvent {

    private String eventId;
    private String eventType;
    private String source;
    private LocalDateTime occurredAt;
    private Payload payload;

    public CertificadoEmitidoEvent(Long idDiagnostico, Long idVeterinario, Long idMascota, String detalleCertificado) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = "CertificadoEmitido";
        this.source = "ms-atencion-clinica";
        this.occurredAt = LocalDateTime.now();
        this.payload = new Payload(idDiagnostico, idVeterinario, idMascota, detalleCertificado);
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
        private String detalleCertificado;

        public Payload(Long idDiagnostico, Long idVeterinario, Long idMascota, String detalleCertificado) {
            this.idDiagnostico = idDiagnostico;
            this.idVeterinario = idVeterinario;
            this.idMascota = idMascota;
            this.detalleCertificado = detalleCertificado;
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

        public String getDetalleCertificado() {
            return detalleCertificado;
        }
    }
}