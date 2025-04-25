package com.waduclay.homefinder.user;

import static org.junit.jupiter.api.Assertions.*;



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

    private DefaultUserSetup service;

    @BeforeEach
    void setUp() {
        service = new DefaultUserSetup(passwordEncoder, repository);
    }

    @Test
    void execute_shouldCreateDefaultUserWhenNotExists() {
        // Arrange
        String username = "defaultAdmin";
        String password = "SecurePass123!";
        String encryptedPassword = "encryptedSecurePass123!";

        when(repository.existsByRole(Role.DEFAULT)).thenReturn(false);
        when(passwordEncoder.encrypt(password)).thenReturn(encryptedPassword);

        // Act
        service.execute(username, password);

        // Assert
        verify(repository).existsByRole(Role.DEFAULT);
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
        String username = "defaultAdmin";
        String password = "SecurePass123!";

        when(repository.existsByRole(Role.DEFAULT)).thenReturn(true);

        // Act
        service.execute(username, password);

        // Assert
        verify(repository).existsByRole(Role.DEFAULT);
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }

    @Test
    void execute_shouldHandleEmptyUsername() {
        // Arrange
        String username = "";
        String password = "SecurePass123!";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.execute(username, password));

        verify(repository, never()).existsByRole(any());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }

    @Test
    void execute_shouldHandleNullUsername() {
        // Arrange
        String username = null;
        String password = "SecurePass123!";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.execute(username, password));

        verify(repository, never()).existsByRole(any());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }

    @Test
    void execute_shouldHandleEmptyPassword() {
        // Arrange
        String username = "defaultAdmin";
        String password = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.execute(username, password));

        verify(repository, never()).existsByRole(any());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }

    @Test
    void execute_shouldHandleNullPassword() {
        // Arrange
        String username = "defaultAdmin";
        String password = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.execute(username, password));

        verify(repository, never()).existsByRole(any());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }

    @Test
    void execute_shouldHandleInvalidUsernameFormat() {
        // Arrange
        String username = "adm"; // Too short (min 4 chars)
        String password = "SecurePass123!";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.execute(username, password));

        verify(repository, never()).existsByRole(any());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }

    @Test
    void execute_shouldHandleReservedUsername() {
        // Arrange
        String username = "admin"; // Reserved name
        String password = "SecurePass123!";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.execute(username, password));

        verify(repository, never()).existsByRole(any());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }

    @Test
    void execute_shouldHandleInvalidPasswordFormat() {
        // Arrange
        String username = "defaultAdmin";
        String password = "weak"; // Doesn't meet complexity requirements

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.execute(username, password));

        verify(repository, never()).existsByRole(any());
        verify(repository, never()).save(any());
        verify(passwordEncoder, never()).encrypt(any());
    }

    @Test
    void execute_shouldStoreUsernameInLowerCase() {
        // Arrange
        String username = "DefaultAdmin";
        String password = "SecurePass123!";
        String encryptedPassword = "encryptedSecurePass123!";

        when(repository.existsByRole(Role.DEFAULT)).thenReturn(false);
        when(passwordEncoder.encrypt(password)).thenReturn(encryptedPassword);

        // Act
        service.execute(username, password);

        // Assert
        ArgumentCaptor<BaseUser> userCaptor = ArgumentCaptor.forClass(BaseUser.class);
        verify(repository).save(userCaptor.capture());

        BaseUser savedUser = userCaptor.getValue();
        assertEquals("defaultadmin", savedUser.getUsername());
    }

    @Test
    void execute_shouldSetCorrectAccountStatusForDefaultUser() {
        // Arrange
        String username = "defaultAdmin";
        String password = "SecurePass123!";
        String encryptedPassword = "encryptedSecurePass123!";

        when(repository.existsByRole(Role.DEFAULT)).thenReturn(false);
        when(passwordEncoder.encrypt(password)).thenReturn(encryptedPassword);

        // Act
        service.execute(username, password);

        // Assert
        ArgumentCaptor<BaseUser> userCaptor = ArgumentCaptor.forClass(BaseUser.class);
        verify(repository).save(userCaptor.capture());

        BaseUser savedUser = userCaptor.getValue();
        assertFalse(savedUser.isCredentialsExpired(), "Credentials should not be expired");
        assertFalse(savedUser.isAccountLocked(), "Account should not be locked");
        assertTrue(savedUser.isAccountActive(), "Account should be active");
    }
}
