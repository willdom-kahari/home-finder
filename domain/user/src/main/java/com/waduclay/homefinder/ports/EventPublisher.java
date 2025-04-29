package com.waduclay.homefinder.ports;

import com.waduclay.homefinder.shared.DomainEvent;

/**
 * Interface for publishing domain events.
 * This interface follows the port-adapter pattern and allows the domain to publish events
 * without knowing the details of how they are delivered to subscribers.
 * 
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public interface EventPublisher {
    
    /**
     * Publishes a domain event.
     * 
     * @param event the domain event to publish
     */
    void publish(DomainEvent event);
    
    /**
     * Publishes multiple domain events.
     * 
     * @param events the domain events to publish
     */
    void publishAll(Iterable<DomainEvent> events);
}
