package com.waduclay.homefinder.shared;

import com.waduclay.homefinder.ports.PasswordEncoder;
import com.waduclay.homefinder.shared.auth.Password;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordTest {

    @Mock
    private PasswordEncoder mockEncoder;

    // Password class tests
    @Test
    void of_createsPasswordWithEncryptedValue() {
        when(mockEncoder.encode("ValidPass123!")).thenReturn("encryptedValue");

        Password password = Password.of("ValidPass123!", mockEncoder);

        assertEquals("encryptedValue", password.getValue());
        verify(mockEncoder).encode("ValidPass123!");
    }

    @Test
    void of_usesDefaultValidatorWhenNotSpecified() {
        when(mockEncoder.encode("ValidPass123!")).thenReturn("encryptedValue");

        Password password = Password.of("ValidPass123!", mockEncoder);

        assertNotNull(password);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "   "})
    void of_rejectsNullOrEmptyPasswords(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> Password.of(input, mockEncoder));
    }

    @Test
    void of_rejectsInvalidPasswordsBasedOnValidator() {
        Password.PasswordValidator strictValidator = Password.PasswordValidator.builder()
                .minLength(12)
                .requireUppercase()
                .requireLowercase()
                .requireDigit()
                .requireSpecialChar()
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> Password.of("short", strictValidator, mockEncoder));
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "ValidPass123!",
            "AnotherValid1@",
            "LongEnoughPassword123#"
    })
    void defaultValidator_acceptsValidPasswords(String password) {
        Password.PasswordValidator validator = Password.PasswordValidator.defaultValidator();
        assertDoesNotThrow(() -> validator.validate(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "short",                // Too short
            "nouppercase123!",      // Missing uppercase
            "NOLOWERCASE123!",      // Missing lowercase
            "NoDigitsHere!",        // Missing digits
            "MissingSpecial123",    // Missing special char
            "ThisPasswordIsWayTooLongForTheDefaultValidatorRequirements123!" // Too long
    })
    void defaultValidator_rejectsInvalidPasswords(String password) {
        Password.PasswordValidator validator = Password.PasswordValidator.defaultValidator();
        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(password));
    }


    @Test
    void validatorBuilder_throwsOnInvalidConfiguration() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Password.PasswordValidator.builder().minLength(0).build()),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> Password.PasswordValidator.builder().minLength(10).maxLength(5).build())
        );
    }

    @Test
    void validator_providesDescriptiveErrorMessage() {
        Password.PasswordValidator validator = Password.PasswordValidator.defaultValidator();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> validator.validate("weak"));

        assertTrue(exception.getMessage().contains("Password must be"));
        assertTrue(exception.getMessage().contains("uppercase"));
        assertTrue(exception.getMessage().contains("lowercase"));
        assertTrue(exception.getMessage().contains("digit"));
        assertTrue(exception.getMessage().contains("special character"));
    }

    // Edge cases
    @Test
    void validator_handlesNullInput() {
        Password.PasswordValidator validator = Password.PasswordValidator.defaultValidator();
        assertThrows(IllegalArgumentException.class,
                () -> validator.validate(null));
    }

    @Test
    void password_isImmutable() {
        when(mockEncoder.encode("ValidPass123!")).thenReturn("encrypted");
        Password password = Password.of("ValidPass123!", mockEncoder);
        String value = password.getValue();
        value = "modified";

        assertEquals("encrypted", password.getValue());
    }
}
