package com.waduclay.homefinder.baseuser;


import com.waduclay.homefinder.shared.*;

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
    private AuthenticationProvider authenticationProvider;

    private BaseUser(final Username username, final Role role, Password password, UserAccountStatus userAccountStatus, AuthenticationProvider authenticationProvider, UUID id) {
        super(id);
        this.username = username;
        this.role = role;
        this.password = password;
        this.userAccountStatus = userAccountStatus;
        this.failedLoginAttempts = 0;
        this.authenticationProvider = authenticationProvider;
    }

    // In BaseUser:
    public static BaseUser createDefaultUser(Username username, Password password) {
        InputGuard.againstNull(username, "username");
        InputGuard.againstNull(password, "password");
        return new BaseUser(username, Role.DEFAULT, password, UserAccountStatus.active(), AuthenticationProvider.APP, UUID.randomUUID());
    }

    public static BaseUser createUser(Username username, Password password, AuthenticationProvider authenticationProvider, UUID id) {
        InputGuard.againstNull(username, "username");
        InputGuard.againstNull(password, "password");
        InputGuard.againstNull(authenticationProvider, "authentication provider");
        InputGuard.againstNull(id, "id");
        return new BaseUser(username, Role.USER, password, UserAccountStatus.inactive(), authenticationProvider, id);
    }

    public static BaseUser createAdmin(Username username, Password password, UUID id) {
        InputGuard.againstNull(username, "username");
        InputGuard.againstNull(password, "password");
        InputGuard.againstNull(id, "id");
        return new BaseUser(username, Role.ADMIN, password, UserAccountStatus.active(), AuthenticationProvider.APP, id);
    }


    public void recordFailedLoginAttempt() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts == 3) {
            this.userAccountStatus = UserAccountStatus.locked();
        }
    }

    public void resetLoginAttempt() {
        this.failedLoginAttempts = 0;
    }

    public void unlockAccount() {
        this.userAccountStatus = UserAccountStatus.active();
        this.failedLoginAttempts = 0;
    }

    public void changePassword(Password newPassword) {
        this.password = newPassword;
        this.userAccountStatus = UserAccountStatus.active();
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
        return userAccountStatus.isCredentialsExpired();
    }

    public boolean isAccountLocked() {
        return userAccountStatus.isAccountLocked();
    }

    public boolean isAccountActive() {
        return userAccountStatus.isAccountActive();
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public AuthenticationProvider getAuthenticationProvider() {
        return this.authenticationProvider;
    }
}
