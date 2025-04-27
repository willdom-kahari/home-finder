package com.waduclay.homefinder.shared;

import com.waduclay.homefinder.ports.UsernamePolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsernameTest {

    @Mock
    private UsernamePolicy mockPolicy;

    // Test the DefaultUsernamePolicy implementation
    @Test
    void defaultPolicy_acceptsValidUsername() {
        UsernamePolicy policy = UsernamePolicy.defaultUsernamePolicy();
        assertDoesNotThrow(() -> Username.of("valid.user-123", policy));
    }

    @ParameterizedTest
    @ValueSource(strings = {"adm", "x", "a", "123"})
    void defaultPolicy_rejectsTooShortUsernames(String username) {
        UsernamePolicy policy = UsernamePolicy.defaultUsernamePolicy();
        assertThrows(IllegalArgumentException.class,
                () -> Username.of(username, policy));
    }

    @ParameterizedTest
    @ValueSource(strings = {"thisusernameistoolongforthepolicy", "abcdefghijklmnopqrstuvwxyz"})
    void defaultPolicy_rejectsTooLongUsernames(String username) {
        UsernamePolicy policy = UsernamePolicy.defaultUsernamePolicy();
        assertThrows(IllegalArgumentException.class,
                () -> Username.of(username, policy));
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@name", "user#name", "user name", "user/name"})
    void defaultPolicy_rejectsInvalidCharacters(String username) {
        UsernamePolicy policy = UsernamePolicy.defaultUsernamePolicy();
        assertThrows(IllegalArgumentException.class,
                () -> Username.of(username, policy));
    }

    @ParameterizedTest
    @ValueSource(strings = {"admin", "ADMIN", "root", "Root", "SYSTEM"})
    void defaultPolicy_rejectsReservedNames(String username) {
        UsernamePolicy policy = UsernamePolicy.defaultUsernamePolicy();
        assertThrows(IllegalArgumentException.class,
                () -> Username.of(username, policy));
    }

    @ParameterizedTest
    @ValueSource(strings = {"fuckyou", "holySHIT", "user_fuck", "shit.user"})
    void defaultPolicy_rejectsOffensiveTerms(String username) {
        UsernamePolicy policy = UsernamePolicy.defaultUsernamePolicy();
        assertThrows(IllegalArgumentException.class,
                () -> Username.of(username, policy));
    }

    // Test the ConfigurableUsernamePolicy
    @Test
    void configurablePolicy_respectsCustomRules() {

        UsernamePolicy policy = UsernamePolicy.configurableUsernamePolicy()
                .withLengthRange(3, 10)
                .withReservedNames("custom")
                .withOffensiveTerms("badword")
                .build();

        // Should accept these
        assertDoesNotThrow(() -> Username.of("abc", policy));
        assertDoesNotThrow(() -> Username.of("1234567890", policy));

        // Should reject these
        assertThrows(IllegalArgumentException.class, () -> Username.of("ab", policy));
        assertThrows(IllegalArgumentException.class, () -> Username.of("12345678901", policy));
        assertThrows(IllegalArgumentException.class, () -> Username.of("custom", policy));
        assertThrows(IllegalArgumentException.class, () -> Username.of("badword", policy));
    }

    // Test Username class behavior
    @Test
    void of_createsUsernameWithLowerCaseValue() {
        Username username = Username.of("TestUser", mockPolicy);
        assertEquals("testuser", username.getValue());
    }

    @Test
    void of_delegatesValidationToPolicy() {
        String input = "validUser";
        Username.of(input, mockPolicy);
        verify(mockPolicy, times(1)).validate(input);
    }

    @Test
    void of_throwsWhenPolicyValidationFails() {
        String input = "invalidUser";

        doThrow(new IllegalArgumentException("Invalid username")).when(mockPolicy).validate(input);
        assertThrows(IllegalArgumentException.class,
                () -> Username.of(input, mockPolicy));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void of_rejectsNullOrEmptyUsernames(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> Username.of(input, mockPolicy));

        verifyNoInteractions(mockPolicy);
    }

    @Test
    void of_throwsWhenPolicyIsNull() {
        assertThrows(NullPointerException.class,
                () -> Username.of("validUser", null));
    }

    @Test
    void equals_ignoresCase() {


        Username user1 = Username.of("user1", mockPolicy);
        Username user1Upper = Username.of("USER1", mockPolicy);
        Username user2 = Username.of("user2", mockPolicy);

        assertEquals(user1, user1Upper);
        assertEquals(user1Upper, user1);
        assertNotEquals(user1, user2);
    }

    @Test
    void hashCode_isCaseInsensitive() {
        Username user1 = Username.of("user1", mockPolicy);
        Username user1Upper = Username.of("USER1", mockPolicy);

        assertEquals(user1.hashCode(), user1Upper.hashCode());
    }

    @Test
    void username_isImmutable() {
        Username username = Username.of("test", mockPolicy);
        String value = username.getValue();

        value = "modified";

        assertEquals("test", username.getValue());
    }
}
