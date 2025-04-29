package com.waduclay.homefinder.shared;

import java.time.Instant;
import java.util.UUID;

/**
 * Base interface for all domain events in the system.
 * Domain events represent something significant that happened in the domain.
 * 
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public interface DomainEvent {
    /**
     * Gets the unique identifier for this event.
     * 
     * @return the event ID
     */
    UUID getEventId();
    
    /**
     * Gets the timestamp when this event occurred.
     * 
     * @return the event timestamp
     */
    Instant getTimestamp();
    
    /**
     * Gets the type of this event.
     * 
     * @return the event type
     */
    String getEventType();
}
