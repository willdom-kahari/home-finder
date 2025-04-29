package com.waduclay.homefinder.users.events;

import com.waduclay.homefinder.shared.AbstractDomainEvent;

import java.util.UUID;

/**
 * Event that is raised when a user account is locked due to too many failed login attempts.
 * 
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class AccountLockedEvent extends AbstractDomainEvent {
    private final UUID userId;
    private final String username;

    /**
     * Creates a new AccountLockedEvent.
     * 
     * @param userId the ID of the user whose account was locked
     * @param username the username of the user whose account was locked
     */
    public AccountLockedEvent(UUID userId, String username) {
        super("ACCOUNT_LOCKED");
        this.userId = userId;
        this.username = username;
    }

    /**
     * Gets the ID of the user whose account was locked.
     * 
     * @return the user ID
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Gets the username of the user whose account was locked.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
