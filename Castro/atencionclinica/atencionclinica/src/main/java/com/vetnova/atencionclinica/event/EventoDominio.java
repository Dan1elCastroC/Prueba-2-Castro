package com.vetnova.atencionclinica.event;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EventoDominio<T> {
    private String eventId;
    private String eventType;
    private String source;
    private String occurredAt;
    private T payload;

    public EventoDominio(String eventType, String source, T payload) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = eventType;
        this.source = source;
        this.occurredAt = LocalDateTime.now().toString();
        this.payload = payload;
    }
}