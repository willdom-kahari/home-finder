package com.waduclay.homefinder.user;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public final class UserAccountStatus {
    private final boolean credentialsExpired;
    private final boolean accountLocked;
    private final boolean accountActive;

    private UserAccountStatus(boolean credentialsExpired, boolean accountLocked, boolean accountActive) {
        this.credentialsExpired = credentialsExpired;
        this.accountLocked = accountLocked;
        this.accountActive = accountActive;
    }

    // Factory methods for clear intent
    public static UserAccountStatus active() {
        return new UserAccountStatus(false, false, true);
    }

    public static UserAccountStatus inactive() {
        return new UserAccountStatus(false, false, false);
    }

    public static UserAccountStatus locked() {
        return new UserAccountStatus(false, true, false);
    }

    public static UserAccountStatus expired() {
        return new UserAccountStatus(true, false, false);
    }

    public boolean isCredentialsExpired() {
        return this.credentialsExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public boolean isAccountActive() {
        return accountActive;
    }
}
