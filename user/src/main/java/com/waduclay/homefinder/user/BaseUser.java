package com.waduclay.homefinder.user;


import com.waduclay.homefinder.shared.Entity;

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

    // In BaseUser:
    static BaseUser createDefaultUser(Username username, Password password) {

        return new BaseUser(username, Role.DEFAULT, password, UserAccountStatus.active());
    }

    static BaseUser createUser(Username username, Password password) {
        return new BaseUser(username, Role.USER, password, UserAccountStatus.inactive());
    }

    static BaseUser createAdmin(Username username, Password password) {
        return new BaseUser(username, Role.ADMIN, password, UserAccountStatus.active());
    }


    void recordFailedLoginAttempt() {

        if (this.failedLoginAttempts >= 2) {
            this.userAccountStatus = UserAccountStatus.locked();
        } else this.failedLoginAttempts++;
    }

    void resetLoginAttempt() {
        this.failedLoginAttempts = 0;
    }


    public void changePassword(Password newPassword) {
        this.password = newPassword;
        this.userAccountStatus = UserAccountStatus.active();
    }

    String getUsername() {
        return username.getValue();
    }

    String getRole() {
        return role.name();
    }

    String getPassword() {
        return password.getValue();
    }

    boolean isCredentialsExpired() {
        return userAccountStatus.isCredentialsExpired();
    }

    boolean isAccountLocked() {
        return userAccountStatus.isAccountLocked();
    }

    boolean isAccountActive() {
        return userAccountStatus.isAccountActive();
    }

    int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }
}
