package com.waduclay.homefinder.genericuser;

import com.waduclay.homefinder.shared.*;
import com.waduclay.homefinder.users.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

class UserTest {
    private UUID testId;
    private NationalIdNumber testNationalId;
    private Name testFirstName;
    private Name testLastName;
    private MobileNumber testMobileNumber;
    private Email testEmail;
    private User user;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testNationalId = new NationalIdNumber("1234567890");
        testFirstName = new Name("John");
        testLastName = new Name("Doe");
        testMobileNumber = new MobileNumber("+1234567890");
        testEmail = new Email("john.doe@example.com");
        user = User.of(testId, testNationalId, testFirstName, testLastName, testMobileNumber, testEmail);
    }

    @Test
    @DisplayName("User creation with valid parameters")
    void of_WithValidParameters_CreatesUser() {
        assertNotNull(user);
        assertEquals(testId, user.getId());
        assertEquals(testNationalId.getValue(), user.getNationalIdNumber());
        assertEquals(testFirstName.getValue(), user.getFirstName());
        assertEquals(testLastName.getValue(), user.getLastName());
        assertEquals(testMobileNumber.getValue(), user.getMobileNumber());
        assertEquals(testEmail.getValue(), user.getEmail());
    }

    @Test
    @DisplayName("Update user with valid parameters")
    void updateUser_WithValidParameters_UpdatesFields() {
        NationalIdNumber newNationalId = new NationalIdNumber("0987654321");
        Name newFirstName = new Name("Jane");
        Name newLastName = new Name("Smith");
        MobileNumber newMobileNumber = new MobileNumber("+0987654321");
        Email newEmail = new Email("jane.smith@example.com");

        user.updateUser(newNationalId, newFirstName, newLastName, newMobileNumber, newEmail);

        assertEquals(newNationalId.getValue(), user.getNationalIdNumber());
        assertEquals(newFirstName.getValue(), user.getFirstName());
        assertEquals(newLastName.getValue(), user.getLastName());
        assertEquals(newMobileNumber.getValue(), user.getMobileNumber());
        assertEquals(newEmail.getValue(), user.getEmail());
    }

    @Test
    @DisplayName("Add national ID URL when none exists")
    void addNationalIdUrl_WhenNoneExists_AddsUrl() {
        Url testUrl = new Url("https://example.com/national-id");
        user.addNationalIdUrl(testUrl);
        assertEquals(testUrl.getValue(), user.getNationalIdUrl());
    }

    @Test
    @DisplayName("Add national ID URL when one already exists throws exception")
    void addNationalIdUrl_WhenAlreadyExists_ThrowsException() {
        Url testUrl1 = new Url("https://example.com/national-id1");
        Url testUrl2 = new Url("https://example.com/national-id2");

        user.addNationalIdUrl(testUrl1);
        assertThrows(IllegalStateException.class, () -> user.addNationalIdUrl(testUrl2));
    }

    @Test
    @DisplayName("Add payslip URL when none exists")
    void addPayslipUrl_WhenNoneExists_AddsUrl() {
        Url testUrl = new Url("https://example.com/payslip");
        user.addPayslipUrl(testUrl);
        assertEquals(testUrl.getValue(), user.getCurrentPayslipUrl());
    }

    @Test
    @DisplayName("Add payslip URL when one already exists throws exception")
    void addPayslipUrl_WhenAlreadyExists_ThrowsException() {
        Url testUrl1 = new Url("https://example.com/payslip1");
        Url testUrl2 = new Url("https://example.com/payslip2");

        user.addPayslipUrl(testUrl1);
        assertThrows(IllegalStateException.class, () -> user.addPayslipUrl(testUrl2));
    }

    @Test
    @DisplayName("Get national ID URL when not set returns null")
    void getNationalIdUrl_WhenNotSet_ReturnsNull() {
        assertThrows(NullPointerException.class, ()-> user.getNationalIdUrl());
    }

    @Test
    @DisplayName("Get payslip URL when not set returns null")
    void getCurrentPayslipUrl_WhenNotSet_ReturnsNull() {
        assertThrows(NullPointerException.class, ()-> user.getCurrentPayslipUrl());
    }
}
