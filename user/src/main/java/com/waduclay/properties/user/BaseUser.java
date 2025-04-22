package com.waduclay.properties.user;


import com.waduclay.properties.shared.Entity;

import java.util.UUID;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class BaseUser extends Entity<UUID> {
    private final Username username;
    private final Role role;
    private final Password password;
    private final UserAccountStatus userAccountStatus;

    private BaseUser(final Username username, final Role role, final Password password, final UserAccountStatus userAccountStatus) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.role = role;
        this.password = password;
        this.userAccountStatus = userAccountStatus;
    }

    public static BaseUser of(String username, Role role, String password, UserAccountStatus userAccountStatus){
        return new BaseUser(
                Username.from(username),
                role,
                Password.from(password),
                userAccountStatus
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
}
