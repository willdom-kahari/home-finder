//package com.waduclay.homefinder.user;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
// */
//class PasswordTest {
//    @Test
//    void shouldFailWhenPasswordIsBlank(){
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Password.from(""));
//        assertEquals("password must not be null or empty", exception.getMessage());
//    }
//
//    @Test
//    void shouldFailWhenPasswordIsNull(){
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Password.from(null));
//        assertEquals("password must not be null or empty", exception.getMessage());
//    }
//
//    @Test
//    void shouldFailWhenPasswordIsLessThanEightCharacters(){
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Password.from("1Aa@"));
//        assertEquals("Password must be 8-30 characters long with at least: 1 uppercase, 1 lowercase, 1 digit, 1 special character", exception.getMessage());
//    }
//
//
//    @Test
//    void shouldFailWhenPasswordIsMoreThanThirtyCharacters() {
//        String longPassword = "A1@abcdefghijklmnopqrstuvwxyz1234"; // 31 chars
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> Password.from(longPassword)
//        );
//        assertEquals(
//                "Password must be 8-30 characters long with at least: 1 uppercase, 1 lowercase, 1 digit, 1 special character",
//                exception.getMessage()
//        );
//    }
//
//    @Test
//    void shouldFailWhenPasswordHasNoUppercase() {
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> Password.from("password1@")
//        );
//        assertEquals(
//                "Password must be 8-30 characters long with at least: 1 uppercase, 1 lowercase, 1 digit, 1 special character",
//                exception.getMessage()
//        );
//    }
//
//    @Test
//    void shouldFailWhenPasswordHasNoLowercase() {
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> Password.from("PASSWORD1@")
//        );
//        assertEquals(
//                "Password must be 8-30 characters long with at least: 1 uppercase, 1 lowercase, 1 digit, 1 special character",
//                exception.getMessage()
//        );
//    }
//
//    @Test
//    void shouldFailWhenPasswordHasNoDigit() {
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> Password.from("Password@")
//        );
//        assertEquals(
//                "Password must be 8-30 characters long with at least: 1 uppercase, 1 lowercase, 1 digit, 1 special character",
//                exception.getMessage()
//        );
//    }
//
//    @Test
//    void shouldFailWhenPasswordHasNoSpecialCharacter() {
//        IllegalArgumentException exception = assertThrows(
//                IllegalArgumentException.class,
//                () -> Password.from("Password1")
//        );
//        assertEquals(
//                "Password must be 8-30 characters long with at least: 1 uppercase, 1 lowercase, 1 digit, 1 special character",
//                exception.getMessage()
//        );
//    }
//
//    @Test
//    void shouldPassWhenPasswordIsExactlyEightCharacters() {
//        assertDoesNotThrow(() -> Password.from("A1@bcdef")); // 8 chars
//    }
//
//    @Test
//    void shouldPassWhenPasswordIsExactlyThirtyCharacters() {
//        String maxLengthPassword = "A1@" + "a".repeat(23) + "bcd"; // 30 chars
//        assertDoesNotThrow(() -> Password.from(maxLengthPassword));
//    }
//
//    @Test
//    void shouldPassWhenPasswordHasAllRequiredCharacters() {
//        assertDoesNotThrow(() -> Password.from("ValidPass1!"));
//    }
//
//    @Test
//    void shouldPassForValidSpecialCharacters() {
//        String[] specialChars = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")"};
//        for (String ch : specialChars) {
//            String password = "Pass1" + ch + "word";
//            assertDoesNotThrow(() -> Password.from(password));
//        }
//    }
//
//}
