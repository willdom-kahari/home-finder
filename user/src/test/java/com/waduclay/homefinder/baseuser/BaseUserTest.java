//package com.waduclay.homefinder.baseuser;
//
//import com.waduclay.homefinder.shared.*;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//class BaseUserTest {
//
//    private final UUID TEST_ID = UUID.randomUUID();
//    private final Username TEST_USERNAME = Username.of("testuser", UsernamePolicy.defaultUsernamePolicy());
//    private final Password TEST_PASSWORD = Password.of("encryptedPass123");
//
//    @Test
//    void createDefaultUser_createsUserWithCorrectProperties() {
//        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
//
//        assertAll(
//                () -> assertEquals("testuser", user.getUsername()),
//                () -> assertEquals("DEFAULT", user.getRole()),
//                () -> assertEquals("encryptedPass123", user.getPassword()),
//                () -> assertTrue(user.isAccountActive()),
//                () -> assertFalse(user.isAccountLocked()),
//                () -> assertFalse(user.isCredentialsExpired()),
//                () -> assertEquals(0, user.getFailedLoginAttempts())
//        );
//    }
//
//    @Test
//    void createUser_createsUserWithCorrectProperties() {
//        BaseUser user = BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD,
//                AuthenticationProvider.GOOGLE, TEST_ID);
//
//        assertAll(
//                () -> assertEquals(TEST_ID, user.getId()),
//                () -> assertEquals("testuser", user.getUsername()),
//                () -> assertEquals("USER", user.getRole()),
//                () -> assertFalse(user.isAccountActive()), // Should be inactive by default
//                () -> assertEquals(AuthenticationProvider.GOOGLE, user.authenticationProvider)
//        );
//    }
//
//    @Test
//    void createAdmin_createsAdminWithCorrectProperties() {
//        BaseUser admin = BaseUser.createAdmin(TEST_USERNAME, TEST_PASSWORD, TEST_ID);
//
//        assertAll(
//                () -> assertEquals(TEST_ID, admin.getId()),
//                () -> assertEquals("ADMIN", admin.getRole()),
//                () -> assertTrue(admin.isAccountActive()),
//                () -> assertEquals(AuthenticationProvider.APP, admin.authenticationProvider)
//        );
//    }
//
//    @Test
//    void recordFailedLoginAttempt_incrementsCounterAndLocksAfterThreshold() {
//        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
//
//        // First two attempts
//        user.recordFailedLoginAttempt();
//        assertEquals(1, user.getFailedLoginAttempts());
//        assertFalse(user.isAccountLocked());
//
//        user.recordFailedLoginAttempt();
//        assertEquals(2, user.getFailedLoginAttempts());
//        assertFalse(user.isAccountLocked());
//
//        // Third attempt should lock
//        user.recordFailedLoginAttempt();
//        assertTrue(user.isAccountLocked());
//        // Counter should still be 2 (not incremented after lock)
//        assertEquals(2, user.getFailedLoginAttempts());
//    }
//
//    @Test
//    void resetLoginAttempt_clearsFailedAttempts() {
//        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
//        user.recordFailedLoginAttempt();
//        user.recordFailedLoginAttempt();
//
//        user.resetLoginAttempt();
//        assertEquals(0, user.getFailedLoginAttempts());
//    }
//
//    @Test
//    void changePassword_updatesPasswordAndActivatesAccount() {
//        BaseUser user = BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD,
//                AuthenticationProvider.APP, TEST_ID);
//        Password newPassword = Password.of("newEncryptedPass");
//
//        user.changePassword(newPassword);
//
//        assertAll(
//                () -> assertEquals("newEncryptedPass", user.getPassword()),
//                () -> assertTrue(user.isAccountActive()),
//                () -> assertFalse(user.isAccountLocked())
//        );
//    }
//
//    @Test
//    void accountStatusMethods_reflectUserAccountStatus() {
//        // Test expired credentials
//        BaseUser expiredUser = BaseUser.(TEST_USERNAME, Role.USER, TEST_PASSWORD,
//                UserAccountStatus.expired(), AuthenticationProvider.APP, TEST_ID);
//        assertTrue(expiredUser.isCredentialsExpired());
//
//        // Test locked account
//        BaseUser lockedUser = new BaseUser(TEST_USERNAME, Role.USER, TEST_PASSWORD,
//                UserAccountStatus.locked(), AuthenticationProvider.APP, TEST_ID);
//        assertTrue(lockedUser.isAccountLocked());
//
//        // Test inactive account
//        BaseUser inactiveUser = new BaseUser(TEST_USERNAME, Role.USER, TEST_PASSWORD,
//                UserAccountStatus.inactive(), AuthenticationProvider.APP, TEST_ID);
//        assertFalse(inactiveUser.isAccountActive());
//    }
//
//    @Test
//    void getters_returnCorrectValues() {
//        BaseUser user = BaseUser.createDefaultUser(TEST_USERNAME, TEST_PASSWORD);
//
//        assertAll(
//                () -> assertEquals("testuser", user.getUsername()),
//                () -> assertEquals("DEFAULT", user.getRole()),
//                () -> assertEquals("encryptedPass123", user.getPassword())
//        );
//    }
//
//    @Test
//    void createUser_withDifferentAuthenticationProviders() {
//        BaseUser googleUser = BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD,
//                AuthenticationProvider.GOOGLE, TEST_ID);
//        assertEquals(AuthenticationProvider.GOOGLE, googleUser.authenticationProvider);
//
//        BaseUser facebookUser = BaseUser.createUser(TEST_USERNAME, TEST_PASSWORD,
//                AuthenticationProvider.GITHUB, TEST_ID);
//        assertEquals(AuthenticationProvider.GITHUB, facebookUser.authenticationProvider);
//    }
//}
