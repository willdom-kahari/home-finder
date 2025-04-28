package com.waduclay.homefinder.users;

import com.waduclay.homefinder.enums.Role;
import com.waduclay.homefinder.ports.*;
import com.waduclay.homefinder.shared.Password;
import com.waduclay.homefinder.shared.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */

class DefaultUserSetupIntegrationTest {

    private final Map<String, BaseUser> baseUserMap = new HashMap<>();
    private final Map<String, User> userMap = new HashMap<>();
    private BaseUserRepositoryPort mapRepository;
    private PasswordEncoderPort base64Encoder;
    private RegisterUser service;
    private BaseUserQueryPort queryPort;

    @BeforeEach
    void setUp() {
        mapRepository = new InMemoryBaseUserRepository(baseUserMap);
        base64Encoder = new Base64PasswordEncoder();
        queryPort = new InMemoryMapUserQuery(baseUserMap);
        PasswordGeneratorPort passwordGenerator = new PasswordGenerator();
        UserRepositoryPort userRepository = new InMemoryUserRepository(userMap);
        service = new RegisterUser(base64Encoder, mapRepository, queryPort, passwordGenerator, userRepository);
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
        baseUserMap.put("existingDefault", existingDefaultUser);

        String username = "newDefault";
        String password = "P@55w0rd";

        // When
        service.ensureDefaultUserExists(username, password);

        // Then - no new user should be created
        Optional<BaseUser> newUser = queryPort.findByUsername(username);
        assertFalse(newUser.isPresent(), "No new default user should be created");

        // Verify existing user is unchanged
        assertEquals(1, baseUserMap.size());
        assertTrue(baseUserMap.containsKey("existingDefault"));
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
        assertEquals(1, baseUserMap.size());
        assertTrue(baseUserMap.containsKey("testuser"));
        assertEquals(user, baseUserMap.get("testuser"));
    }

    // Implement the test ports using Base64 and Map
    static class Base64PasswordEncoder implements PasswordEncoderPort {
        @Override
        public String encrypt(String plainText) {
            return Base64.getEncoder().encodeToString(plainText.getBytes());
        }
    }

    static class InMemoryBaseUserRepository implements BaseUserRepositoryPort {
        private final Map<String, BaseUser> baseUserMap;

        public InMemoryBaseUserRepository(Map<String, BaseUser> baseUserMap) {
            this.baseUserMap = baseUserMap;
        }


        @Override
        public void save(BaseUser user) {
            baseUserMap.put(user.getUsername(), user);
        }
    }

    static class InMemoryMapUserQuery implements BaseUserQueryPort {
        private final Map<String, BaseUser> baseUserMap;

        public InMemoryMapUserQuery(Map<String, BaseUser> baseUserMap) {
            this.baseUserMap = baseUserMap;
        }

        @Override
        public boolean existsByRole(Role role) {
            Collection<BaseUser> values = baseUserMap.values();
            return values.stream().anyMatch(user -> user.getRole().contains(role.name()));
        }

        @Override
        public Optional<BaseUser> findByUsername(String username) {
            return Optional.ofNullable(baseUserMap.get(username));
        }

        @Override
        public boolean existsByUsername(String username) {
            return Optional.ofNullable(baseUserMap.get(username)).isPresent();
        }
    }
    static class PasswordGenerator implements PasswordGeneratorPort{
        @Override
        public String generate() {
            return "";
        }
    }

    static class InMemoryUserRepository implements UserRepositoryPort {
        private final Map<String, User> userMap;

        public InMemoryUserRepository(Map<String, User> userMap) {
            this.userMap = userMap;
        }


        @Override
        public void save(User user) {
            userMap.put(user.getId().toString(), user);
        }
    }
}
