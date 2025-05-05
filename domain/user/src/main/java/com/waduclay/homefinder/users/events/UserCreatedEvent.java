package com.waduclay.homefinder.users.events;

import com.waduclay.homefinder.shared.AbstractDomainEvent;

import java.util.UUID;

/**
 * Event that is raised when a new user is created.
 *
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class UserCreatedEvent extends AbstractDomainEvent {
    private final UUID userId;
    private final String username;

    /**
     * Creates a new UserCreatedEvent.
     *
     * @param userId   the ID of the user that was created
     * @param username the username of the user that was created
     */
    public UserCreatedEvent(UUID userId, String username) {
        super("USER_CREATED");
        this.userId = userId;
        this.username = username;
    }

    /**
     * Gets the ID of the user that was created.
     *
     * @return the user ID
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Gets the username of the user that was created.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
