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

    // In BaseUser:
    static BaseUser createDefaultUser(Username username, Password password) {

        return new BaseUser(username, Role.DEFAULT, password, UserAccountStatus.active());
    }

    static BaseUser of(Username username, Role role, String password, UserAccountStatus userAccountStatus, PasswordEncoderPort passwordEncoderPort) {

        Objects.requireNonNull(role, "Role cannot be null");
        Objects.requireNonNull(userAccountStatus, "User account status cannot be null");

        return new BaseUser(
                username,
                role,
                Password.from(password, passwordEncoderPort),
                userAccountStatus
        );
    }

    static BaseUser of(Username username, String password, PasswordEncoderPort passwordEncoderPort) {
        return of(
                username,
                Role.USER,
                password,
                UserAccountStatus.inactive(),
                passwordEncoderPort
        );
    }

    void recordFailedLoginAttempt() {

        if (this.failedLoginAttempts >= 2) {
            this.userAccountStatus = UserAccountStatus.locked();
        } else this.failedLoginAttempts++;
    }

    void resetLoginAttempt() {
        this.failedLoginAttempts = 0;
    }


    void updatePassword(String newPassword, PasswordEncoderPort passwordEncoderPort) {
        this.password = Password.from(newPassword, passwordEncoderPort);  // Update in-place
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
