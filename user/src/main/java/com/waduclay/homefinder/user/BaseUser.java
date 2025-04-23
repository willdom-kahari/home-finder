package com.waduclay.homefinder.user;


import com.waduclay.homefinder.shared.Entity;

import java.util.Objects;
import java.util.UUID;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class BaseUser extends Entity<UUID> {
    private final Username username;
    private final Role role;
    private Password password;
    private UserAccountStatus userAccountStatus;
    private int failedLoginAttempts;

    private BaseUser(final Username username, final Role role, Password password, UserAccountStatus userAccountStatus) {
        super(UUID.randomUUID());
        this.username = username;
        this.role = role;
        this.password = password;
        this.userAccountStatus = userAccountStatus;
        this.failedLoginAttempts = 0;
    }

    public static BaseUser of(String username, Role role, String password, UserAccountStatus userAccountStatus) {

        Objects.requireNonNull(role, "Role cannot be null");
        Objects.requireNonNull(userAccountStatus, "User account status cannot be null");

        return new BaseUser(
                Username.from(username),
                role,
                Password.from(password),
                userAccountStatus
        );
    }

    public static BaseUser of(String username, String password) {
        return of(
                username,
                Role.USER,
                password,
                new UserAccountStatus(false, false, false)
        );
    }

    public void recordFailedLoginAttempt() {

        if (this.failedLoginAttempts >= 2) {
            this.userAccountStatus = new UserAccountStatus(
                    userAccountStatus.credentialsExpired(),
                    true,  // Lock account
                    userAccountStatus.accountActive()
            );
        } else this.failedLoginAttempts++;
    }

    public void resetLoginAttempt() {
        this.failedLoginAttempts = 0;
    }


    public void updatePassword(String newPassword) {
        this.password = Password.from(newPassword);  // Update in-place
        this.userAccountStatus = new UserAccountStatus(
                false,  // Reset credentialsExpired
                this.userAccountStatus.accountLocked(),
                this.userAccountStatus.accountActive()
        );
    }

    public String getUsername() {
        return username.getValue();
    }

    public String getRole() {
        return role.name();
    }

    public String getPassword() {
        return password.getValue();
    }

    public boolean isCredentialsExpired() {
        return userAccountStatus.credentialsExpired();
    }

    public boolean isAccountLocked() {
        return userAccountStatus.accountLocked();
    }

    public boolean isAccountActive() {
        return userAccountStatus.accountActive();
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }
}
