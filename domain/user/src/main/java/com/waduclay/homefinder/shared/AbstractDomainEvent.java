package com.waduclay.homefinder.shared;

import java.time.Instant;
import java.util.UUID;

/**
 * Abstract base class for all domain events.
 * Provides common implementation for the DomainEvent interface.
 * 
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public abstract class AbstractDomainEvent implements DomainEvent {
    private final UUID eventId;
    private final Instant timestamp;
    private final String eventType;

    /**
     * Creates a new domain event with the specified event type.
     * 
     * @param eventType the type of the event
     */
    protected AbstractDomainEvent(String eventType) {
        this.eventId = UUID.randomUUID();
        this.timestamp = Instant.now();
        this.eventType = eventType;
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String getEventType() {
        return eventType;
    }
}
