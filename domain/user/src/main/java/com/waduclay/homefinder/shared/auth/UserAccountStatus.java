package com.waduclay.homefinder.shared.auth;


import java.util.Objects;

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

    public static UserAccountStatus of(boolean active, boolean accountLocked, boolean credentialsExpired) {
        if (active) {
            return active();
        } else if (accountLocked) {
            return locked();
        } else if (credentialsExpired) {
            return expired();
        } else {
            return inactive();
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccountStatus that = (UserAccountStatus) o;
        return credentialsExpired == that.credentialsExpired &&
                accountLocked == that.accountLocked &&
                accountActive == that.accountActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentialsExpired, accountLocked, accountActive);
    }
}
