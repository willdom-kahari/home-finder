package com.waduclay.homefinder.shared;

import com.waduclay.homefinder.shared.personal.NationalIdNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class NationalIdNumberTest {

    // Test valid ID formats
    @ParameterizedTest
    @ValueSource(strings = {
            "12345678A12",    // Standard format
            "123456789B12",   // With 9 digits before letter
            " 12345678A12 ",  // With whitespace
            "123-456-78A-12", // With hyphens
            "12345678a12"     // Lowercase letter
    })
    void from_acceptsValidFormats(String input) {
        assertDoesNotThrow(() -> NationalIdNumber.from(input));
    }

    // Test normalization
    @Test
    void from_normalizesWhitespace() {
        NationalIdNumber id = NationalIdNumber.from(" 12345678A12 ");
        assertEquals("12345678A12", id.getValue());
    }

    @Test
    void from_normalizesHyphens() {
        NationalIdNumber id = NationalIdNumber.from("123-456-78A-12");
        assertEquals("12345678A12", id.getValue());
    }

    @Test
    void from_normalizesCase() {
        NationalIdNumber id = NationalIdNumber.from("12345678a12");
        assertEquals("12345678A12", id.getValue());
    }

    // Test invalid inputs
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", " ", "   "})
    void from_rejectsNullOrEmpty(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> NationalIdNumber.from(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "1234567A12",      // Too few digits before letter
            "1234567890C12",   // Too many digits before letter
            "12345678A123",    // Too many digits after letter
            "12345678A1",      // Too few digits after letter
            "12345678 12",     // Space instead of letter
            "12345678*12",     // Special character instead of letter
            "ABCDEFGHIJKL",    // All letters
    })
    void from_rejectsInvalidFormats(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> NationalIdNumber.from(input));
    }

    // Test object behavior
    @Test
    void getValue_returnsNormalizedValue() {
        NationalIdNumber id = NationalIdNumber.from("123-456-78A-12");
        assertEquals("12345678A12", id.getValue());
    }

    // Test object contract
    @Test
    void equalsAndHashCode_consistentForSameId() {
        NationalIdNumber id1 = NationalIdNumber.from("12345678A12");
        NationalIdNumber id2 = NationalIdNumber.from("12345678A12");

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equalsAndHashCode_differentForDifferentIds() {
        NationalIdNumber id1 = NationalIdNumber.from("12345678A12");
        NationalIdNumber id2 = NationalIdNumber.from("98765432Z99");

        assertNotEquals(id1, id2);
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void equals_returnsFalseForDifferentClass() {
        NationalIdNumber id = NationalIdNumber.from("12345678A12");
        assertNotEquals("12345678A12", id);
    }

    @Test
    void equals_returnsFalseForNull() {
        NationalIdNumber id = NationalIdNumber.from("12345678A12");
        assertNotEquals(null, id);
    }

    // Test immutability
    @Test
    void isImmutable() {
        NationalIdNumber id = NationalIdNumber.from("12345678A12");
        String value = id.getValue();
        value = "modified";

        assertEquals("12345678A12", id.getValue());
    }

    @Test
    void toString_returnsValue() {
        NationalIdNumber id = NationalIdNumber.from("12345678A12");
        assertEquals("12345678A12", id.toString());
    }

    // Test error messages
    @Test
    void from_throwsDescriptiveErrorMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> NationalIdNumber.from("invalid"));

        assertTrue(exception.getMessage().contains("Invalid National ID format"));
    }
}
