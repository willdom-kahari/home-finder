package com.waduclay.homefinder.users.events;

import com.waduclay.homefinder.shared.AbstractDomainEvent;

import java.util.UUID;

/**
 * Event that is raised when a user's information is updated.
 *
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class UserUpdatedEvent extends AbstractDomainEvent {
    private final UUID userId;
    private final String username;

    /**
     * Creates a new UserUpdatedEvent.
     *
     * @param userId   the ID of the user that was updated
     * @param username the username of the user that was updated
     */
    public UserUpdatedEvent(UUID userId, String username) {
        super("USER_UPDATED");
        this.userId = userId;
        this.username = username;
    }

    /**
     * Gets the ID of the user that was updated.
     *
     * @return the user ID
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Gets the username of the user that was updated.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
