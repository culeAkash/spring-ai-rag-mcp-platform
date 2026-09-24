package com.docmind.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class ProcessedEvent {

    @Id
    private UUID eventId;
    private LocalDateTime processedAt;

    public ProcessedEvent(UUID eventId ) {
        this.eventId = eventId;
        this.processedAt = LocalDateTime.now();
    }
}
