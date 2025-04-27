package com.waduclay.homefinder.user;

import static org.junit.jupiter.api.Assertions.*;

import com.waduclay.homefinder.enums.Role;
import com.waduclay.homefinder.users.BaseUser;
import com.waduclay.homefinder.ports.BaseUserQueryPort;
import com.waduclay.homefinder.ports.BaseUserRepositoryPort;
import com.waduclay.homefinder.users.DefaultUserSetup;
import com.waduclay.homefinder.ports.PasswordEncoderPort;
import com.waduclay.homefinder.ports.UsernamePolicy;
import com.waduclay.homefinder.shared.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */

class DefaultUserSetupIntegrationTest {

    private final Map<String, BaseUser> userMap = new HashMap<>();
    private BaseUserRepositoryPort mapRepository;
    private PasswordEncoderPort base64Encoder;
    private DefaultUserSetup service;
    private BaseUserQueryPort queryPort;

    @BeforeEach
    void setUp() {
        mapRepository = new InMemoryMapUserRepository(userMap);
        base64Encoder = new Base64PasswordEncoder();
        queryPort = new InMemoryMapUserQuery(userMap);
        service = new DefaultUserSetup(base64Encoder, mapRepository, queryPort);
    }

    @Test
    void shouldCreateDefaultUserWhenNoneExists() {
        // Given
        String username = "dAdmin";
        String password = "P@55w0rd";

        // When
        service.ensureDefaultUserExists(username, password);

        // Then
        Optional<BaseUser> savedUser = queryPort.findByUsername(username.toLowerCase());
        assertTrue(savedUser.isPresent(), "User should be saved in repository");

        BaseUser user = savedUser.get();
        assertEquals(username.toLowerCase(), user.getUsername());
        assertEquals(Role.DEFAULT.name(), user.getRole());

        // Verify password was encoded with Base64
        String expectedEncodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
        assertEquals(expectedEncodedPassword, user.getPassword());

        // Verify account status
        assertFalse(user.isAccountLocked());
        assertFalse(user.isCredentialsExpired());
        assertTrue(user.isAccountActive());
    }

    @Test
    void shouldNotCreateDefaultUserWhenOneAlreadyExists() {
        // Given - an existing default user
        BaseUser existingDefaultUser = BaseUser.createDefaultUser(
                Username.of("existingDefault", new UsernamePolicy.DefaultUsernamePolicy()),
                Password.of("P@55w0rd", base64Encoder)
        );
        userMap.put("existingDefault", existingDefaultUser);

        String username = "newDefault";
        String password = "P@55w0rd";

        // When
        service.ensureDefaultUserExists(username, password);

        // Then - no new user should be created
        Optional<BaseUser> newUser = queryPort.findByUsername(username);
        assertFalse(newUser.isPresent(), "No new default user should be created");

        // Verify existing user is unchanged
        assertEquals(1, userMap.size());
        assertTrue(userMap.containsKey("existingDefault"));
    }

    @Test
    void shouldEncodePasswordUsingBase64() {
        // Given
        String password = "testPassword@123";

        // When
        String encoded = base64Encoder.encrypt(password);

        // Then
        String expected = Base64.getEncoder().encodeToString(password.getBytes());
        assertEquals(expected, encoded);
    }

    @Test
    void shouldSaveUserInMapRepository() {
        // Given
        BaseUser user = BaseUser.createAdmin(
                Username.of("testUser", new UsernamePolicy.DefaultUsernamePolicy()),
                Password.of("P@55w0rd", base64Encoder)
        );

        // When
        mapRepository.save(user);

        // Then
        assertEquals(1, userMap.size());
        assertTrue(userMap.containsKey("testuser"));
        assertEquals(user, userMap.get("testuser"));
    }

    // Implement the test ports using Base64 and Map
    static class Base64PasswordEncoder implements PasswordEncoderPort {
        @Override
        public String encrypt(String plainText) {
            return Base64.getEncoder().encodeToString(plainText.getBytes());
        }
    }

    static class InMemoryMapUserRepository implements BaseUserRepositoryPort {
        private final Map<String, BaseUser> userMap;

        public InMemoryMapUserRepository(Map<String, BaseUser> userMap) {
            this.userMap = userMap;
        }


        @Override
        public void save(BaseUser user) {
            userMap.put(user.getUsername(), user);
        }
    }

    static class InMemoryMapUserQuery implements BaseUserQueryPort {
        private final Map<String, BaseUser> userMap;

        public InMemoryMapUserQuery(Map<String, BaseUser> userMap) {
            this.userMap = userMap;
        }

        @Override
        public boolean existsByRole(Role role) {
            Collection<BaseUser> values = userMap.values();
            return values.stream().anyMatch(user -> user.getRole().contains(role.name()));
        }

        @Override
        public Optional<BaseUser> findByUsername(String username) {
            return Optional.ofNullable(userMap.get(username));
        }

        @Override
        public boolean existsByUsername(String username) {
            return Optional.ofNullable(userMap.get(username)).isPresent();
        }
    }
}
