package com.waduclay.homefinder.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */

class UsernameTest {

    // Test valid usernames
    @ParameterizedTest
    @ValueSource(strings = {"validUser", "user123", "user.name", "user-name", "longusername12345678"})
    void shouldCreateUsernameForValidInput(String input) {
        assertDoesNotThrow(() -> Username.from(input));
        Username username = Username.from(input);
        assertEquals(input.toLowerCase(), username.getValue());
    }

    // Test empty/null usernames
    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectEmptyOrNullUsernames(String input) {
        assertThrows(IllegalArgumentException.class, () -> Username.from(input));
    }

    // Test length validation
    @ParameterizedTest
    @ValueSource(strings = {"usr", "a", ""})
    void shouldRejectTooShortUsernames(String input) {
        assertThrows(IllegalArgumentException.class, () -> Username.from(input));
    }

    @Test
    void shouldRejectTooLongUsernames() {
        String longUsername = "a".repeat(21);
        assertThrows(IllegalArgumentException.class, () -> Username.from(longUsername));
    }

    // Test character validation
    @ParameterizedTest
    @ValueSource(strings = {"user@name", "user#name", "user name", "user/name"})
    void shouldRejectInvalidCharacters(String input) {
        assertThrows(IllegalArgumentException.class, () -> Username.from(input));
    }

    // Test reserved usernames
    @ParameterizedTest
    @ValueSource(strings = {"admin", "ADMIN", "root", "Root", "system", "support"})
    void shouldRejectReservedUsernames(String input) {
        assertThrows(IllegalArgumentException.class, () -> Username.from(input));
    }

    // Test offensive language
    @ParameterizedTest
    @ValueSource(strings = {"fuckyou", "shithead", "adminFucker"})
    void shouldRejectOffensiveUsernames(String input) {
        assertThrows(IllegalArgumentException.class, () -> Username.from(input));
    }

    // Test case insensitivity
    @Test
    void shouldTreatUsernamesCaseInsensitively() {
        Username username1 = Username.from("TestUser");
        Username username2 = Username.from("testuser");
        assertEquals(username1, username2);
        assertEquals(username1.hashCode(), username2.hashCode());
    }

    // Test equality
    @Test
    void shouldCorrectlyImplementEquals() {
        Username username1 = Username.from("user1");
        Username username2 = Username.from("user1");
        Username username3 = Username.from("user2");

        assertEquals(username1, username2);
        assertNotEquals(username1, username3);
        assertNotEquals(null, username1);
        assertNotEquals("user1", username1); // Different class
    }

    // Test value retrieval
    @Test
    void shouldReturnCorrectValue() {
        String input = "TestUser123";
        Username username = Username.from(input);
        assertEquals(input.toLowerCase(), username.getValue());
    }
}
