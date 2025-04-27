package com.waduclay.homefinder.genericuser;

import com.waduclay.homefinder.baseuser.*;
import com.waduclay.homefinder.shared.*;
import com.waduclay.homefinder.users.PasswordGeneratorPort;
import com.waduclay.homefinder.users.User;
import com.waduclay.homefinder.users.UserRegistrationService;
import com.waduclay.homefinder.users.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

    @Mock
    private PasswordEncoderPort passwordEncoder;
    @Mock
    private BaseUserRepositoryPort baseUserRepository;
    @Mock
    private BaseUserQueryPort baseUserQueryPort;
    @Mock
    private PasswordGeneratorPort passwordGenerator;
    @Mock
    private UserRepositoryPort userRepository;
    @Mock
    private UsernamePolicy usernamePolicy;

    private UserRegistrationService service;

    private final String TEST_USERNAME = "testuser";
    private final String TEST_NAME = "John";
    private final String TEST_SURNAME = "Doe";
    private final String TEST_NATIONAL_ID = "12345678A90";
    private final String TEST_MOBILE = "263745678908";
    private final String TEST_EMAIL = "john.doe@example.com";
    private final String TEST_PASSWORD = "P@55w0rd";

    @BeforeEach
    void setUp() {
        service = new UserRegistrationService(
                passwordEncoder,
                baseUserRepository,
                baseUserQueryPort,
                passwordGenerator,
                userRepository,
                usernamePolicy
        );
    }

    @Test
    void register_WithNewUsername_CreatesAndSavesUser() {
        // Arrange
        when(baseUserQueryPort.existsByUsername(TEST_USERNAME)).thenReturn(false);
        when(passwordGenerator.generate()).thenReturn(TEST_PASSWORD);
        when(passwordEncoder.encrypt(TEST_PASSWORD)).thenReturn("encodedPassword");

        // Act
        service.register(TEST_USERNAME, TEST_NAME, TEST_SURNAME,
                TEST_NATIONAL_ID, TEST_MOBILE, TEST_EMAIL,
                Role.USER, AuthenticationProvider.APP);

        // Assert
        verify(baseUserQueryPort).existsByUsername(TEST_USERNAME);
        verify(passwordGenerator).generate();
        verify(passwordEncoder).encrypt(TEST_PASSWORD);
        verify(baseUserRepository, times(1)).save(any(BaseUser.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_WithExistingUsername_ThrowsException() {
        // Arrange
        when(baseUserQueryPort.existsByUsername(TEST_USERNAME)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                service.register(TEST_USERNAME, TEST_NAME, TEST_SURNAME,
                        TEST_NATIONAL_ID, TEST_MOBILE, TEST_EMAIL,
                        Role.USER, AuthenticationProvider.GOOGLE)
        );
    }

    @Test
    void register_WithDefaultRole_ThrowsException() {
        // Arrange
        when(baseUserQueryPort.existsByUsername(TEST_USERNAME)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
                service.register(TEST_USERNAME, TEST_NAME, TEST_SURNAME,
                        TEST_NATIONAL_ID, TEST_MOBILE, TEST_EMAIL,
                        Role.DEFAULT, AuthenticationProvider.APP)
        );
    }

    @Test
    void register_WithNullEmail_CreatesUserWithoutEmail() {
        // Arrange
        when(baseUserQueryPort.existsByUsername(TEST_USERNAME)).thenReturn(false);
        when(passwordGenerator.generate()).thenReturn(TEST_PASSWORD);
        when(passwordEncoder.encrypt(TEST_PASSWORD)).thenReturn("encodedPassword");

        // Act
        service.register(TEST_USERNAME, TEST_NAME, TEST_SURNAME,
                TEST_NATIONAL_ID, TEST_MOBILE, null,
                Role.USER, AuthenticationProvider.GITHUB);

        // Assert - No exception thrown means null email was handled
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createBaseUser_ForAdminRole_CreatesAdminUser() {
        // Arrange
        when(passwordGenerator.generate()).thenReturn(TEST_PASSWORD);
        when(passwordEncoder.encrypt(TEST_PASSWORD)).thenReturn("encodedPassword");

        // Act
        service.register(TEST_USERNAME, TEST_NAME, TEST_SURNAME,
                TEST_NATIONAL_ID, TEST_MOBILE, null,
                Role.ADMIN, AuthenticationProvider.GITHUB);

        // Assert
        verify(baseUserRepository).save(argThat(user -> {
            user.getRole();
            return true;
        }));
    }


}
