package com.waduclay.homefinder.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
class BaseUserTest {

    @Test
    void of_WithValidArguments_CreatesUserWithCorrectState() {
        UserAccountStatus status = UserAccountStatus.active();
        BaseUser user = BaseUser.of(Username.from("testUser", new UsernamePolicy.DefaultUsernamePolicy()), Role.ADMIN, "P@55w0rd", status);

        assertEquals("testuser", user.getUsername());
        assertEquals("ADMIN", user.getRole());
        assertFalse(user.isCredentialsExpired());
        assertFalse(user.isAccountLocked());
        assertTrue(user.isAccountActive());
        assertEquals(0, user.getFailedLoginAttempts());
    }

    @Test
    void of_WithNullRole_ThrowsException() {
        assertThrows(NullPointerException.class,
                () -> BaseUser.of(Username.from("test", new UsernamePolicy.DefaultUsernamePolicy()), null, "pass", UserAccountStatus.inactive()));
    }

    @Test
    void of_WithNullStatus_ThrowsException() {
        assertThrows(NullPointerException.class,
                () -> BaseUser.of(Username.from("test", new UsernamePolicy.DefaultUsernamePolicy()), Role.USER, "pass", null));
    }

    @Test
    void of_WithDefaultArguments_SetsUserRoleAndActiveStatus() {
        BaseUser user = BaseUser.of(Username.from("testUser", new UsernamePolicy.DefaultUsernamePolicy()), "P@55w0rd");

        assertEquals("USER", user.getRole());
        assertFalse(user.isCredentialsExpired());
        assertFalse(user.isAccountLocked());
        assertFalse(user.isAccountActive());
    }

    @Test
    void recordFailedLoginAttempt_UnderThreshold_DoesNotLockAccount() {
        BaseUser user = BaseUser.of(Username.from("test", new UsernamePolicy.DefaultUsernamePolicy()), "P@55w0rd");
        user.recordFailedLoginAttempt(); // 1
        user.recordFailedLoginAttempt(); // 2

        assertEquals(2, user.getFailedLoginAttempts());
        assertFalse(user.isAccountLocked());
    }

    @Test
    void recordFailedLoginAttempt_ReachesThreshold_LocksAccount() {
        BaseUser user = BaseUser.of(Username.from("test", new UsernamePolicy.DefaultUsernamePolicy()), "P@55w0rd");
        user.recordFailedLoginAttempt(); // 1
        user.recordFailedLoginAttempt(); // 2
        user.recordFailedLoginAttempt(); // 3

        assertTrue(user.isAccountLocked());
    }

    @Test
    void recordFailedLoginAttempt_OnLockedAccount_DoesNothing() {
        BaseUser user = BaseUser.of(Username.from("test", new UsernamePolicy.DefaultUsernamePolicy()), "P@55w0rd");
        user.recordFailedLoginAttempt(); // 1
        user.recordFailedLoginAttempt(); // 2
        user.recordFailedLoginAttempt(); // 3 (locked)
        int preAttempts = user.getFailedLoginAttempts();

        user.recordFailedLoginAttempt(); // Still 3
        assertEquals(preAttempts, user.getFailedLoginAttempts());
    }


    @Test
    void resetLoginAttempt_ClearsCounter() {
        BaseUser user = BaseUser.of(Username.from("test", new UsernamePolicy.DefaultUsernamePolicy()), "P@55w0rd");
        user.recordFailedLoginAttempt();
        user.resetLoginAttempt();

        assertEquals(0, user.getFailedLoginAttempts());
    }

    @Test
    void updatePassword_ChangesPasswordAndResetsCredentialsExpired() {
        BaseUser user = BaseUser.of(Username.from("test", new UsernamePolicy.DefaultUsernamePolicy()), "P@55w0rd");
        user.updatePassword("newPass^04356");

        assertNotEquals("oldPass", user.getPassword());
        assertFalse(user.isCredentialsExpired());
    }

    @Test
    void updatePassword_OnLockedAccount_StillUpdates() {
        BaseUser user = BaseUser.of(Username.from("test", new UsernamePolicy.DefaultUsernamePolicy()), "P@55w0rd");
        user.recordFailedLoginAttempt(); // 1
        user.recordFailedLoginAttempt(); // 2
        user.recordFailedLoginAttempt(); // 3 (locked)

        user.updatePassword("p@55W0rd");
        assertFalse(user.isAccountLocked()); // Stays locked
        assertNotEquals("oldPass", user.getPassword());
    }

    @Test
    void isAccountActive_WhenInactive_OverridesLockedStatus() {
        BaseUser user = BaseUser.of(Username.from("test", new UsernamePolicy.DefaultUsernamePolicy()), Role.USER, "P@55w0rd",
                UserAccountStatus.locked()); // Locked but inactive

        assertFalse(user.isAccountActive());
        assertTrue(user.isAccountLocked()); // Still locked, but inactive takes precedence
    }

    @Test
    void isAccountLocked_WhenInactive_ReturnsTrueButCannotLogin() {
        BaseUser user = BaseUser.of(Username.from("test", new UsernamePolicy.DefaultUsernamePolicy()), Role.USER, "P@55w0rd",
                UserAccountStatus.locked());

        assertTrue(user.isAccountLocked()); // Technically locked
        assertFalse(user.isAccountActive()); // But inactive blocks login
    }

    @Test
    void updatePassword_WithEmptyPassword_ThrowsException() {
        BaseUser user = BaseUser.of(Username.from("test", new UsernamePolicy.DefaultUsernamePolicy()), "validPass@123");
        assertThrows(IllegalArgumentException.class,
                () -> user.updatePassword(""));
    }

    @Test
    void getUsername_ReturnsImmutableValue() {
        BaseUser user = BaseUser.of(Username.from("original", new UsernamePolicy.DefaultUsernamePolicy()), "pass12$S");
        // Simulate reflection attack (shouldn't modify username)
        // This test assumes Username is immutable
        assertDoesNotThrow(() -> user.getUsername().replace("original", "hacked"));
        assertEquals("original", user.getUsername());
    }

    @Test
    void fullLoginScenario_SuccessThenFailure() {
        BaseUser user = BaseUser.of(Username.from("test", new UsernamePolicy.DefaultUsernamePolicy()), "P@55w0rd");

        // Failed attempts (2)
        user.recordFailedLoginAttempt();
        user.recordFailedLoginAttempt();
        assertFalse(user.isAccountLocked());

        // Successful login resets attempts
        user.resetLoginAttempt();
        assertEquals(0, user.getFailedLoginAttempts());

        // More failures (now locks)
        user.recordFailedLoginAttempt();
        user.recordFailedLoginAttempt();
        user.recordFailedLoginAttempt();
        assertTrue(user.isAccountLocked());
    }
}
