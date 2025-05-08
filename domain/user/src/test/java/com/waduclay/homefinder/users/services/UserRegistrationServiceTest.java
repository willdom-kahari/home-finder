package com.waduclay.homefinder.users.services;


import com.waduclay.homefinder.ports.*;
import com.waduclay.homefinder.shared.DomainEvent;
import com.waduclay.homefinder.shared.auth.enums.AuthenticationProvider;
import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.shared.exceptions.UserAlreadyExistsException;
import com.waduclay.homefinder.shared.exceptions.ValidationException;
import com.waduclay.homefinder.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for UserRegistrationService
 */
@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserCommand userCommand;

    @Mock
    private UserQuery userQuery;

    @Mock
    private PasswordGenerator passwordGenerator;

    @Mock
    private EventPublisher eventPublisher;

    private UserRegistrationService service;

    @BeforeEach
    void setUp() {
        // Use the real UsernamePolicy implementation
        UsernamePolicy usernamePolicy = UsernamePolicy.defaultUsernamePolicy();

        service = new UserRegistrationService(
                passwordEncoder,
                userCommand,
                userQuery,
                passwordGenerator,
                usernamePolicy,
                eventPublisher
        );
    }

    // Tests for registerUser method

    @Test
    void registerUser_shouldCreateUserSuccessfully() {
        // Arrange
        String username = "testuser";
        String firstName = "Test";
        String lastName = "User";
        String nationalId = "12345678A12"; // Valid format: 8 digits + letter + 2 digits
        String mobileNumber = "0712345678"; // Valid format: 10 digits starting with 07
        String email = "test@example.com";
        AuthenticationProvider provider = AuthenticationProvider.APP;

        // Password that meets requirements: 8+ chars, uppercase, lowercase, digit, special char
        String generatedPassword = "P@ssw0rd123!";

        List<DomainEvent> events = List.of(mock(DomainEvent.class));

        when(userQuery.existsByUsername(username)).thenReturn(false);
        when(passwordGenerator.generate()).thenReturn(generatedPassword);
        // Return the actual UserAggregate that's passed to save
        when(userCommand.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // Mock the domain events for any UserAggregate
//        doReturn(events).when(eventPublisher).publishAll(any());

        // Act
        User result = service.registerUser(
                username, firstName, lastName, nationalId, mobileNumber, email, provider
        );

        // Assert
        verify(userQuery).existsByUsername(username);
        verify(passwordGenerator).generate();
        verify(userCommand).save(any(User.class));
        verify(eventPublisher).publishAll(any());

        // Verify the result is not null and has the expected properties
        assertNotNull(result);
        // Additional assertions could be added here if needed
    }

    @Test
    void registerUser_shouldThrowExceptionWhenUsernameExists() {
        // Arrange
        String username = "existinguser";
        String firstName = "Test";
        String lastName = "User";
        String nationalId = "1234567890";
        String mobileNumber = "+1234567890";
        String email = "test@example.com";
        AuthenticationProvider provider = AuthenticationProvider.APP;

        when(userQuery.existsByUsername(username)).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () ->
                service.registerUser(username, firstName, lastName, nationalId, mobileNumber, email, provider)
        );

        verify(userQuery).existsByUsername(username);
        verify(userCommand, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void registerUser_shouldThrowExceptionWhenUsernameIsInvalid(String username) {
        // Arrange
        String firstName = "Test";
        String lastName = "User";
        String nationalId = "1234567890";
        String mobileNumber = "+1234567890";
        String email = "test@example.com";
        AuthenticationProvider provider = AuthenticationProvider.APP;

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                service.registerUser(username, firstName, lastName, nationalId, mobileNumber, email, provider)
        );

        verify(userCommand, never()).save(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void registerUser_shouldThrowExceptionWhenFirstNameIsInvalid(String firstName) {
        // Arrange
        String username = "testuser";
        String lastName = "User";
        String nationalId = "1234567890";
        String mobileNumber = "+1234567890";
        String email = "test@example.com";
        AuthenticationProvider provider = AuthenticationProvider.APP;

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                service.registerUser(username, firstName, lastName, nationalId, mobileNumber, email, provider)
        );

        verify(userCommand, never()).save(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void registerUser_shouldThrowExceptionWhenLastNameIsInvalid(String lastName) {
        // Arrange
        String username = "testuser";
        String firstName = "Test";
        String nationalId = "1234567890";
        String mobileNumber = "+1234567890";
        String email = "test@example.com";
        AuthenticationProvider provider = AuthenticationProvider.APP;

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                service.registerUser(username, firstName, lastName, nationalId, mobileNumber, email, provider)
        );

        verify(userCommand, never()).save(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void registerUser_shouldThrowExceptionWhenNationalIdIsInvalid(String nationalId) {
        // Arrange
        String username = "testuser";
        String firstName = "Test";
        String lastName = "User";
        String mobileNumber = "+1234567890";
        String email = "test@example.com";
        AuthenticationProvider provider = AuthenticationProvider.APP;

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                service.registerUser(username, firstName, lastName, nationalId, mobileNumber, email, provider)
        );

        verify(userCommand, never()).save(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void registerUser_shouldThrowExceptionWhenMobileNumberIsInvalid(String mobileNumber) {
        // Arrange
        String username = "testuser";
        String firstName = "Test";
        String lastName = "User";
        String nationalId = "1234567890";
        String email = "test@example.com";
        AuthenticationProvider provider = AuthenticationProvider.APP;

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                service.registerUser(username, firstName, lastName, nationalId, mobileNumber, email, provider)
        );

        verify(userCommand, never()).save(any());
    }

    // Tests for ensureDefaultUserExists method

    @Test
    void ensureDefaultUserExists_shouldCreateDefaultUserWhenNotExists() {
        // Arrange
        String username = "dadmin"; // Shorter username (within 4-10 chars)
        String password = "SecurePass123!";

        User user = mock(User.class);
        List<DomainEvent> events = List.of(mock(DomainEvent.class));

        when(userQuery.existsByRole(Role.DEFAULT)).thenReturn(false);
        when(userCommand.save(any(User.class))).thenReturn(user);
        when(user.getDomainEvents()).thenReturn(events);

        // Act
        service.ensureDefaultUserExists(username, password);

        // Assert
        verify(userQuery).existsByRole(Role.DEFAULT);
        verify(userCommand).save(any(User.class));
        verify(eventPublisher).publishAll(events);
    }

    @Test
    void ensureDefaultUserExists_shouldNotCreateUserWhenDefaultUserAlreadyExists() {
        // Arrange
        String username = "defaultadmin";
        String password = "SecurePass123!";

        when(userQuery.existsByRole(Role.DEFAULT)).thenReturn(true);

        // Act
        service.ensureDefaultUserExists(username, password);

        // Assert
        verify(userQuery).existsByRole(Role.DEFAULT);
        verify(userCommand, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void ensureDefaultUserExists_shouldThrowExceptionWhenUsernameIsInvalid(String username) {
        // Arrange
        String password = "SecurePass123!";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                service.ensureDefaultUserExists(username, password)
        );

        verify(userCommand, never()).save(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void ensureDefaultUserExists_shouldThrowExceptionWhenPasswordIsInvalid(String password) {
        // Arrange
        String username = "defaultadmin";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                service.ensureDefaultUserExists(username, password)
        );

        verify(userCommand, never()).save(any());
    }

    // Tests for createAdminUser method

    @Test
    void createAdminUser_shouldCreateAdminUserSuccessfully() {
        // Arrange
        String username = "adminuser";
        String firstName = "Admin";
        String lastName = "User";
        String nationalId = "12345678B12"; // Valid format: 8 digits + letter + 2 digits
        String mobileNumber = "0723456789"; // Valid format: 10 digits starting with 07
        String email = "admin@example.com";

        // Password that meets requirements: 8+ chars, uppercase, lowercase, digit, special char
        String generatedPassword = "P@ssw0rd123!";

        List<DomainEvent> events = List.of(mock(DomainEvent.class));

        when(userQuery.existsByUsername(username)).thenReturn(false);
        when(passwordGenerator.generate()).thenReturn(generatedPassword);
        // Return the actual UserAggregate that's passed to save
        when(userCommand.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = service.createAdminUser(
                username, firstName, lastName, nationalId, mobileNumber, email
        );

        // Assert
        verify(userQuery).existsByUsername(username);
        verify(passwordGenerator).generate();
        verify(userCommand).save(any(User.class));
        verify(eventPublisher).publishAll(any());

        // Verify the result is not null and has the expected properties
        assertNotNull(result);
        // Additional assertions could be added here if needed
    }

    @Test
    void createAdminUser_shouldThrowExceptionWhenUsernameExists() {
        // Arrange
        String username = "existingadmin";
        String firstName = "Admin";
        String lastName = "User";
        String nationalId = "1234567890";
        String mobileNumber = "+1234567890";
        String email = "admin@example.com";

        when(userQuery.existsByUsername(username)).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () ->
                service.createAdminUser(username, firstName, lastName, nationalId, mobileNumber, email)
        );

        verify(userQuery).existsByUsername(username);
        verify(userCommand, never()).save(any());
        verify(eventPublisher, never()).publishAll(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void createAdminUser_shouldThrowExceptionWhenUsernameIsInvalid(String username) {
        // Arrange
        String firstName = "Admin";
        String lastName = "User";
        String nationalId = "1234567890";
        String mobileNumber = "+1234567890";
        String email = "admin@example.com";

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                service.createAdminUser(username, firstName, lastName, nationalId, mobileNumber, email)
        );

        verify(userCommand, never()).save(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void createAdminUser_shouldThrowExceptionWhenFirstNameIsInvalid(String firstName) {
        // Arrange
        String username = "adminuser";
        String lastName = "User";
        String nationalId = "1234567890";
        String mobileNumber = "+1234567890";
        String email = "admin@example.com";

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                service.createAdminUser(username, firstName, lastName, nationalId, mobileNumber, email)
        );

        verify(userCommand, never()).save(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void createAdminUser_shouldThrowExceptionWhenLastNameIsInvalid(String lastName) {
        // Arrange
        String username = "adminuser";
        String firstName = "Admin";
        String nationalId = "1234567890";
        String mobileNumber = "+1234567890";
        String email = "admin@example.com";

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                service.createAdminUser(username, firstName, lastName, nationalId, mobileNumber, email)
        );

        verify(userCommand, never()).save(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void createAdminUser_shouldThrowExceptionWhenNationalIdIsInvalid(String nationalId) {
        // Arrange
        String username = "adminuser";
        String firstName = "Admin";
        String lastName = "User";
        String mobileNumber = "+1234567890";
        String email = "admin@example.com";

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                service.createAdminUser(username, firstName, lastName, nationalId, mobileNumber, email)
        );

        verify(userCommand, never()).save(any());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void createAdminUser_shouldThrowExceptionWhenMobileNumberIsInvalid(String mobileNumber) {
        // Arrange
        String username = "adminuser";
        String firstName = "Admin";
        String lastName = "User";
        String nationalId = "1234567890";
        String email = "admin@example.com";

        // Act & Assert
        assertThrows(ValidationException.class, () ->
                service.createAdminUser(username, firstName, lastName, nationalId, mobileNumber, email)
        );

        verify(userCommand, never()).save(any());
    }
}
