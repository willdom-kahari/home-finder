package com.waduclay.homefinder.shared;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MobileNumberTest {

    // Test valid number formats
    @ParameterizedTest
    @ValueSource(strings = {
            "263771234567",    // International format
            "+263771234567",   // International format with +
            "0771234567",      // Local format
            "771234567"        // Short format
    })
    void from_acceptsValidFormats(String input) {
        assertDoesNotThrow(() -> MobileNumber.from(input));
    }

    // Test normalisation
    @Test
    void from_normalizesInternationalFormatWithPlus() {
        MobileNumber number = MobileNumber.from("+263771234567");
        assertEquals("263771234567", number.getValue());
    }

    @Test
    void from_normalizesLocalFormat() {
        MobileNumber number = MobileNumber.from("0771234567");
        assertEquals("263771234567", number.getValue());
    }

    @Test
    void from_normalizesShortFormat() {
        MobileNumber number = MobileNumber.from("771234567");
        assertEquals("263771234567", number.getValue());
    }

    @Test
    void from_preservesInternationalFormat() {
        MobileNumber number = MobileNumber.from("263771234567");
        assertEquals("263771234567", number.getValue());
    }

    // Test invalid inputs
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "   "})
    void from_rejectsNullOrEmpty(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> MobileNumber.from(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "123456789",       // Wrong country code
            "26377123456",     // Too short
            "2637712345678",   // Too long
            "26377a234567",    // Contains letters
            "263-771-234567",  // Contains special chars
            "00263771234567",  // Wrong prefix
    })
    void from_rejectsInvalidNumbers(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> MobileNumber.from(input));
    }

    // Test international format conversion
    @Test
    void toInternationalFormat_returnsCorrectValue() {
        MobileNumber number = MobileNumber.from("263771234567");
        assertEquals("+263771234567", number.toInternationalFormat());
    }

    // Test object contract
    @Test
    void equalsAndHashCode_consistentForSameNumber() {
        MobileNumber num1 = MobileNumber.from("263771234567");
        MobileNumber num2 = MobileNumber.from("0771234567");

        assertEquals(num1, num2);
        assertEquals(num1.hashCode(), num2.hashCode());
    }

    @Test
    void equalsAndHashCode_differentForDifferentNumbers() {
        MobileNumber num1 = MobileNumber.from("263771234567");
        MobileNumber num2 = MobileNumber.from("263772234567");

        assertNotEquals(num1, num2);
        assertNotEquals(num1.hashCode(), num2.hashCode());
    }

    @Test
    void equals_returnsFalseForDifferentClass() {
        MobileNumber number = MobileNumber.from("263771234567");
        assertNotEquals("263771234567", number);
    }

    @Test
    void equals_returnsFalseForNull() {
        MobileNumber number = MobileNumber.from("263771234567");
        assertNotEquals(null, number);
    }

    // Test immutability
    @Test
    void isImmutable() {
        MobileNumber number = MobileNumber.from("263771234567");
        String value = number.getValue();
        value = "modified";

        assertEquals("263771234567", number.getValue());
    }

    @Test
    void toString_returnsValue() {
        MobileNumber number = MobileNumber.from("263771234567");
        assertEquals("263771234567", number.toString());
    }
}
