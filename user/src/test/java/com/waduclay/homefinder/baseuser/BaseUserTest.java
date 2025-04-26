package com.waduclay.homefinder.baseuser;

import com.waduclay.homefinder.shared.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseUserTest {
    @Mock
    private PasswordEncoderPort passwordEncoder;
    private final UUID TEST_ID = UUID.randomUUID();
    private Username TEST_USERNAME;
    private Password TEST_PASSWORD;
    @BeforeEach
    void setup(){
        String password = "P@55w0rd";
        String encryptedPassword = "encryptedP@55w0rd";
        when(passwordEncoder.encrypt(password)).thenReturn(encryptedPassword);
        TEST_USERNAME = Username.of("testuser", UsernamePolicy.defaultUsernamePolicy());
        TEST_PASSWORD = Password.of(password, passwordEncoder);
    }

    @Test
    void createDefaultUser_createsUserWithCorrectProperties() {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);

        assertAll(
                () -> assertEquals("testuser", user.getUsername()),
                () -> assertEquals("DEFAULT", user.getRole()),
                () -> assertEquals("encryptedP@55w0rd", user.getPassword()),
                () -> assertTrue(user.isAccountActive()),
                () -> assertFalse(user.isAccountLocked()),
                () -> assertFalse(user.isCredentialsExpired()),
                () -> assertEquals(0, user.getFailedLoginAttempts())
        );
    }

    @Test
    void createUser_createsUserWithCorrectProperties() {
        BaseUser user = BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD,
                AuthenticationProvider.GOOGLE, TEST_ID);

        assertAll(
                () -> assertEquals(TEST_ID, user.getId()),
                () -> assertEquals("testuser", user.getUsername()),
                () -> assertEquals("USER", user.getRole()),
                () -> assertFalse(user.isAccountActive())
        );
    }

    @Test
    void createAdmin_createsAdminWithCorrectProperties() {
        BaseUser admin = BaseUser.createAdmin(TEST_USERNAME, TEST_PASSWORD, TEST_ID);

        assertAll(
                () -> assertEquals(TEST_ID, admin.getId()),
                () -> assertEquals("ADMIN", admin.getRole()),
                () -> assertTrue(admin.isAccountActive())
        );
    }

    @Test
    void recordFailedLoginAttempt_incrementsCounterAndLocksAfterThreshold() {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);

        // First two attempts
        user.recordFailedLoginAttempt();
        assertEquals(1, user.getFailedLoginAttempts());
        assertFalse(user.isAccountLocked());

        user.recordFailedLoginAttempt();
        assertEquals(2, user.getFailedLoginAttempts());
        assertFalse(user.isAccountLocked());

        // Third attempt should lock
        user.recordFailedLoginAttempt();
        assertTrue(user.isAccountLocked());
        // Counter should still be 2 (not incremented after lock)
        assertEquals(2, user.getFailedLoginAttempts());
    }

    @Test
    void resetLoginAttempt_clearsFailedAttempts() {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
        user.recordFailedLoginAttempt();
        user.recordFailedLoginAttempt();

        user.resetLoginAttempt();
        assertEquals(0, user.getFailedLoginAttempts());
    }

    @Test
    void changePassword_updatesPasswordAndActivatesAccount() {
        BaseUser user = BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD,
                AuthenticationProvider.APP, TEST_ID);
        String newPassword = "newP@55w0rd";
        String encryptedNewPassword = "encryptedNewP@55w0rd";

        when(passwordEncoder.encrypt(newPassword)).thenReturn(encryptedNewPassword);
        Password latest = Password.of(newPassword, passwordEncoder);

        user.changePassword(latest);

        assertAll(
                () -> assertEquals(encryptedNewPassword, user.getPassword()),
                () -> assertTrue(user.isAccountActive()),
                () -> assertFalse(user.isAccountLocked())
        );
    }


    @Test
    void getters_returnCorrectValues() {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);

        assertAll(
                () -> assertEquals("testuser", user.getUsername()),
                () -> assertEquals("DEFAULT", user.getRole()),
                () -> assertEquals("encryptedP@55w0rd", user.getPassword())
        );
    }

}
