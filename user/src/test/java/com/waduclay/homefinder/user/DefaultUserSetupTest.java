package com.waduclay.homefinder.user;

import static org.junit.jupiter.api.Assertions.*;


import com.waduclay.homefinder.ports.BaseUserQueryPort;
import com.waduclay.homefinder.ports.BaseUserRepositoryPort;
import com.waduclay.homefinder.ports.PasswordEncoderPort;
import com.waduclay.homefinder.enums.Role;
import com.waduclay.homefinder.users.BaseUser;
import com.waduclay.homefinder.users.DefaultUserSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */

@ExtendWith(MockitoExtension.class)
class DefaultUserSetupTest {

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private BaseUserRepositoryPort repository;
    @Mock
    private BaseUserQueryPort query;

    private DefaultUserSetup service;

    @BeforeEach
    void setUp() {
        service = new DefaultUserSetup(passwordEncoder, repository, query);
    }

    @Test
    void execute_shouldCreateDefaultUserWhenNotExists() {
        // Arrange
        String username = "dAdmin";
        String password = "SecurePass123!";
        String encryptedPassword = "encryptedSecurePass123!";

        when(query.existsByRole(Role.DEFAULT)).thenReturn(false);
        when(passwordEncoder.encrypt(password)).thenReturn(encryptedPassword);

        // Act
        service.ensureDefaultUserExists(username, password);

        // Assert
        verify(query).existsByRole(Role.DEFAULT);
        verify(passwordEncoder).encrypt(password);

        ArgumentCaptor<BaseUser> userCaptor = ArgumentCaptor.forClass(BaseUser.class);
        verify(repository).save(userCaptor.capture());

        BaseUser savedUser = userCaptor.getValue();
        assertEquals(username.toLowerCase(), savedUser.getUsername());
        assertEquals(Role.DEFAULT.name(), savedUser.getRole());
        assertEquals(encryptedPassword, savedUser.getPassword());
        assertFalse(savedUser.isCredentialsExpired());
        assertFalse(savedUser.isAccountLocked());
        assertTrue(savedUser.isAccountActive());
    }

    @Test
    void execute_shouldNotCreateUserWhenDefaultUserAlreadyExists() {
        // Arrange
        String username = "dAdmin";
        String password = "SecurePass123!";

        when(query.existsByRole(Role.DEFAULT)).thenReturn(true);

        // Act
        service.ensureDefaultUserExists(username, password);

        // Assert
        verify(query).existsByRole(Role.DEFAULT);
        verify(repository, never()).save(any());
    }

    @Test
    void execute_shouldHandleEmptyUsername() {
        // Arrange
        String username = "";
        String password = "SecurePass123!";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.ensureDefaultUserExists(username, password));

        verify(query, never()).existsByRole(any());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }

    @Test
    void execute_shouldHandleNullUsername() {
        // Arrange
        String username = null;
        String password = "SecurePass123!";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.ensureDefaultUserExists(username, password));

        verify(query, never()).existsByRole(any());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }

    @Test
    void execute_shouldHandleEmptyPassword() {
        // Arrange
        String username = "defaultAdmin";
        String password = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.ensureDefaultUserExists(username, password));

        verify(query, never()).existsByRole(any());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }

    @Test
    void execute_shouldHandleNullPassword() {
        // Arrange
        String username = "defaultAdmin";
        String password = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.ensureDefaultUserExists(username, password));

        verify(query, never()).existsByRole(any());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }

    @Test
    void execute_shouldHandleInvalidUsernameFormat() {
        // Arrange
        String username = "adm"; // Too short (min 4 chars)
        String password = "SecurePass123!";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.ensureDefaultUserExists(username, password));

        verify(query, never()).existsByRole(any());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }


    @Test
    void execute_shouldHandleInvalidPasswordFormat() {
        // Arrange
        String username = "defaultAdmin";
        String password = "weak"; // Doesn't meet complexity requirements

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.ensureDefaultUserExists(username, password));

        verify(query, never()).existsByRole(any());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }

    @Test
    void execute_shouldStoreUsernameInLowerCase() {
        // Arrange
        String username = "DAdmin";
        String password = "SecurePass123!";
        String encryptedPassword = "encryptedSecurePass123!";

        when(query.existsByRole(Role.DEFAULT)).thenReturn(false);
        when(passwordEncoder.encrypt(password)).thenReturn(encryptedPassword);

        // Act
        service.ensureDefaultUserExists(username, password);

        // Assert
        ArgumentCaptor<BaseUser> userCaptor = ArgumentCaptor.forClass(BaseUser.class);
        verify(repository).save(userCaptor.capture());

        BaseUser savedUser = userCaptor.getValue();
        assertEquals("dadmin", savedUser.getUsername());
    }

    @Test
    void execute_shouldSetCorrectAccountStatusForDefaultUser() {
        // Arrange
        String username = "dAdmin";
        String password = "SecurePass123!";
        String encryptedPassword = "encryptedSecurePass123!";

        when(query.existsByRole(Role.DEFAULT)).thenReturn(false);
        when(passwordEncoder.encrypt(password)).thenReturn(encryptedPassword);

        // Act
        service.ensureDefaultUserExists(username, password);

        // Assert
        ArgumentCaptor<BaseUser> userCaptor = ArgumentCaptor.forClass(BaseUser.class);
        verify(repository).save(userCaptor.capture());

        BaseUser savedUser = userCaptor.getValue();
        assertFalse(savedUser.isCredentialsExpired(), "Credentials should not be expired");
        assertFalse(savedUser.isAccountLocked(), "Account should not be locked");
        assertTrue(savedUser.isAccountActive(), "Account should be active");
    }
}
