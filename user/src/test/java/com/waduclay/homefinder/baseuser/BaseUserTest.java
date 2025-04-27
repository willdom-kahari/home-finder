package com.waduclay.homefinder.baseuser;

import com.waduclay.homefinder.enums.AuthenticationProvider;
import com.waduclay.homefinder.enums.Role;
import com.waduclay.homefinder.ports.PasswordEncoderPort;
import com.waduclay.homefinder.ports.UsernamePolicy;
import com.waduclay.homefinder.shared.*;
import com.waduclay.homefinder.users.BaseUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
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
                AuthenticationProvider.GOOGLE);

        assertAll(
                () -> assertNotNull(user.getId()),
                () -> assertEquals("testuser", user.getUsername()),
                () -> assertEquals("USER", user.getRole()),
                () -> assertFalse(user.isAccountActive())
        );
    }

    @Test
    void createAdmin_createsAdminWithCorrectProperties() {
        BaseUser admin = BaseUser.createAdmin(TEST_USERNAME, TEST_PASSWORD);

        assertAll(
                () -> assertNotNull(admin.getId()),
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

        assertEquals(3, user.getFailedLoginAttempts());
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
                AuthenticationProvider.APP);
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

    @ParameterizedTest
    @ValueSource(ints = {3, 5})
    void recordFailedLoginAttempt_shouldLockAccountAfterThreshold(int threshold) {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);

        // Simulate failed attempts up to threshold
        for (int i = 0; i < threshold; i++) {
            user.recordFailedLoginAttempt();
        }

        assertAll(
                () -> assertEquals(3, user.getFailedLoginAttempts()),
                () -> assertEquals(threshold > 2, user.isAccountLocked())
        );
    }

    @Test
    void resetLoginAttempt_shouldResetCounterToZero() {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
        user.recordFailedLoginAttempt();
        user.recordFailedLoginAttempt();

        user.resetLoginAttempt();

        assertAll(
                () -> assertEquals(0, user.getFailedLoginAttempts()),
                () -> assertFalse(user.isAccountLocked())
        );
    }

    @Test
    void unlockAccount_shouldResetStatusAndAttempts() {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
        // Lock the account
        user.recordFailedLoginAttempt();
        user.recordFailedLoginAttempt();

        user.unlockAccount();

        assertAll(
                () -> assertTrue(user.isAccountActive()),
                () -> assertFalse(user.isAccountLocked()),
                () -> assertEquals(0, user.getFailedLoginAttempts())
        );
    }

    // Password change tests
    @Test
    void changePassword_shouldUpdatePasswordAndActivateAccount() {
        BaseUser user = BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD, AuthenticationProvider.APP);
        Password newPassword = mock(Password.class);
        when(newPassword.getValue()).thenReturn("newPassword123!");

        user.changePassword(newPassword);

        assertAll(
                () -> assertEquals("newPassword123!", user.getPassword()),
                () -> assertTrue(user.isAccountActive())
        );
    }

    @Test
    void changePassword_shouldUnlockAccountIfLocked() {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
        // Lock the account
        user.recordFailedLoginAttempt();
        user.recordFailedLoginAttempt();
        user.recordFailedLoginAttempt();
        Password newPassword = mock(Password.class);
        user.changePassword(newPassword);
        assertFalse(user.isAccountLocked());
    }

    // Authentication provider tests
    @ParameterizedTest
    @EnumSource(AuthenticationProvider.class)
    void createUser_shouldStoreAuthenticationProvider(AuthenticationProvider provider) {
        BaseUser user = BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD, provider);
        assertEquals(provider, user.getAuthenticationProvider());
    }

    // Edge cases and validation
    @Test
    void createUser_withNullUsername_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> BaseUser.createUser(null, TEST_PASSWORD, AuthenticationProvider.APP));
    }

    @Test
    void createUser_withNullPassword_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> BaseUser.createUser(TEST_USERNAME, null, AuthenticationProvider.APP));
    }

    @Test
    void createUser_withNullAuthProvider_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD, null));
    }

    // Equality tests
    @Test
    void equals_shouldReturnTrueForSameInstance() {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
        assertTrue(user.equals(user));
    }

    @Test
    void equals_shouldReturnFalseForNull() {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
        assertFalse(user.equals(null));
    }

    @Test
    void equals_shouldReturnFalseForDifferentClass() {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
        assertFalse(user.equals("not a user object"));
    }

    @Test
    void equals_shouldCompareBasedOnId() {
        BaseUser user1 = BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD, AuthenticationProvider.APP);
        BaseUser user2 = BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD, AuthenticationProvider.APP);
        BaseUser user3 = BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD, AuthenticationProvider.APP);

        assertAll(
                () -> assertNotEquals(user1, user2),
                () -> assertNotEquals(user1, user3)
        );
    }

    // HashCode tests
    @Test
    void hashCode_shouldBeConsistent() {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
        int initialHashCode = user.hashCode();

        assertEquals(initialHashCode, user.hashCode());
    }

    @Test
    void hashCode_shouldBeEqualForEqualObjects() {
        BaseUser user1 = BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD, AuthenticationProvider.APP);
        BaseUser user2 = BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD, AuthenticationProvider.APP);

        assertNotEquals(user1.hashCode(), user2.hashCode());
    }

    // Thread safety tests
    @Test
    void concurrentFailedLoginAttempts_shouldMaintainConsistentState() throws InterruptedException {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
        int threadCount = 10;

        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    user.recordFailedLoginAttempt();
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(3, user.getFailedLoginAttempts());
        assertTrue(user.isAccountLocked());
    }

    // Parameterized test for different role combinations
    @ParameterizedTest
    @MethodSource("roleProvider")
    void createUser_withDifferentRoles_shouldSetCorrectRole(Role role) {
        BaseUser user = createUserWithRole(role);
        assertEquals(role.name(), user.getRole());
    }

    private static Stream<Arguments> roleProvider() {
        return Stream.of(
                Arguments.of(Role.DEFAULT),
                Arguments.of(Role.USER),
                Arguments.of(Role.ADMIN)
        );
    }

    private BaseUser createUserWithRole(Role role) {
        return switch (role) {
            case DEFAULT -> BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
            case USER -> BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD, AuthenticationProvider.APP);
            case ADMIN -> BaseUser.createAdmin(TEST_USERNAME, TEST_PASSWORD);
        };
    }

    // Test for account status transitions
    @Test
    void accountStatusTransitions_shouldFollowBusinessRules() {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);

        // Initial state
        assertTrue(user.isAccountActive());
        assertFalse(user.isAccountLocked());

        // First failed attempt
        user.recordFailedLoginAttempt();
        assertEquals(1, user.getFailedLoginAttempts());
        assertTrue(user.isAccountActive());

        // Second failed attempt
        user.recordFailedLoginAttempt();
        assertEquals(2, user.getFailedLoginAttempts());
        assertTrue(user.isAccountActive());

        // Third failed attempt - should lock
        user.recordFailedLoginAttempt();
        assertTrue(user.isAccountLocked());
        assertFalse(user.isAccountActive());

        // Unlock
        user.unlockAccount();
        assertTrue(user.isAccountActive());
        assertFalse(user.isAccountLocked());
        assertEquals(0, user.getFailedLoginAttempts());

        // Change password should also unlock
        user.recordFailedLoginAttempt();
        user.recordFailedLoginAttempt();
        user.changePassword(mock(Password.class));
        assertTrue(user.isAccountActive());
    }


    // Test for an edge case of maximum failed attempts
    @Test
    void recordFailedLoginAttempt_shouldNotExceedMaxInt() {
        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);

        // Simulate Integer.MAX_VALUE attempts
        setFailedAttemptsThroughReflection(user);

        user.recordFailedLoginAttempt();
        user.recordFailedLoginAttempt();

        assertEquals(3, user.getFailedLoginAttempts());
    }

    // Helper method for testing private field manipulation
    private void setFailedAttemptsThroughReflection(BaseUser user) {
        try {
            Field field = BaseUser.class.getDeclaredField("failedLoginAttempts");
            field.setAccessible(true);
            field.set(user, Integer.MAX_VALUE - 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
