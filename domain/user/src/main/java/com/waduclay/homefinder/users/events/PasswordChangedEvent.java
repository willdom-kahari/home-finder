package com.waduclay.homefinder.users.events;

import com.waduclay.homefinder.shared.AbstractDomainEvent;

import java.util.UUID;

/**
 * Event that is raised when a user's password is changed.
 * 
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class PasswordChangedEvent extends AbstractDomainEvent {
    private final UUID userId;
    private final String username;

    /**
     * Creates a new PasswordChangedEvent.
     * 
     * @param userId the ID of the user whose password was changed
     * @param username the username of the user whose password was changed
     */
    public PasswordChangedEvent(UUID userId, String username) {
        super("PASSWORD_CHANGED");
        this.userId = userId;
        this.username = username;
    }

    /**
     * Gets the ID of the user whose password was changed.
     * 
     * @return the user ID
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Gets the username of the user whose password was changed.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
