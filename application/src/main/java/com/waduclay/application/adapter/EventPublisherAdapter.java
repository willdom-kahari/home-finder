package com.waduclay.application.adapter;


import com.waduclay.homefinder.ports.EventPublisher;
import com.waduclay.homefinder.shared.DomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Component
@RequiredArgsConstructor
public class EventPublisherAdapter implements EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(DomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishAll(Iterable<DomainEvent> events) {
        events.forEach(this::publish);
    }
}
