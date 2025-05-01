package com.waduclay.homefinder.shared;

import com.waduclay.homefinder.shared.auth.UserAccountStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountStatusTest {

    // Test factory methods
    @Test
    void active_createsActiveAccount() {
        UserAccountStatus status = UserAccountStatus.active();

        assertAll(
                () -> assertTrue(status.isAccountActive()),
                () -> assertFalse(status.isAccountLocked()),
                () -> assertFalse(status.isCredentialsExpired())
        );
    }

    @Test
    void inactive_createsInactiveAccount() {
        UserAccountStatus status = UserAccountStatus.inactive();

        assertAll(
                () -> assertFalse(status.isAccountActive()),
                () -> assertFalse(status.isAccountLocked()),
                () -> assertFalse(status.isCredentialsExpired())
        );
    }

    @Test
    void locked_createsLockedAccount() {
        UserAccountStatus status = UserAccountStatus.locked();

        assertAll(
                () -> assertFalse(status.isAccountActive()),
                () -> assertTrue(status.isAccountLocked()),
                () -> assertFalse(status.isCredentialsExpired())
        );
    }

    @Test
    void expired_createsExpiredAccount() {
        UserAccountStatus status = UserAccountStatus.expired();

        assertAll(
                () -> assertFalse(status.isAccountActive()),
                () -> assertFalse(status.isAccountLocked()),
                () -> assertTrue(status.isCredentialsExpired())
        );
    }


    @Test
    void equalsAndHashCode_differentForDifferentStates() {
        UserAccountStatus status1 = UserAccountStatus.active();
        UserAccountStatus status2 = UserAccountStatus.locked();

        assertNotEquals(status1, status2);
        assertNotEquals(status1.hashCode(), status2.hashCode());
    }
}
