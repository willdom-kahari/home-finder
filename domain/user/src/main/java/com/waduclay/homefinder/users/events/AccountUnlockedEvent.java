package com.waduclay.homefinder.users.events;

import com.waduclay.homefinder.shared.AbstractDomainEvent;

import java.util.UUID;

/**
 * Event that is raised when a user account is unlocked.
 * 
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class AccountUnlockedEvent extends AbstractDomainEvent {
    private final UUID userId;
    private final String username;

    /**
     * Creates a new AccountUnlockedEvent.
     * 
     * @param userId the ID of the user whose account was unlocked
     * @param username the username of the user whose account was unlocked
     */
    public AccountUnlockedEvent(UUID userId, String username) {
        super("ACCOUNT_UNLOCKED");
        this.userId = userId;
        this.username = username;
    }

    /**
     * Gets the ID of the user whose account was unlocked.
     * 
     * @return the user ID
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Gets the username of the user whose account was unlocked.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
