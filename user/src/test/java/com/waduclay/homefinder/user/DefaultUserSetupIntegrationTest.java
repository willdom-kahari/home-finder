//package com.waduclay.homefinder.user;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.*;
//
///**
// * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
// */
//
//class DefaultUserSetupIntegrationTest {
//
//    private final Map<String, BaseUser> userMap = new HashMap<>();
//    private BaseUserRepositoryPort mapRepository;
//    private PasswordEncoderPort base64Encoder;
//    private DefaultUserSetup service;
//    private BaseUserQueryPort queryPort;
//
//    @BeforeEach
//    void setUp() {
//        mapRepository = new InMemoryMapUserRepository(userMap);
//        base64Encoder = new Base64PasswordEncoder();
//        queryPort = new InMemoryMapUserQuery(userMap);
//        service = new DefaultUserSetup(base64Encoder, mapRepository, queryPort);
//    }
//
//    @Test
//    void shouldCreateDefaultUserWhenNoneExists() {
//        // Given
//        String username = "defaultAdmin";
//        String password = "P@55w0rd";
//
//        // When
//        service.ensureDefaultUserExists(username, password);
//
//        // Then
//        Optional<BaseUser> savedUser = queryPort.findByUsername(username.toLowerCase());
//        assertTrue(savedUser.isPresent(), "User should be saved in repository");
//
//        BaseUser user = savedUser.get();
//        assertEquals(username.toLowerCase(), user.getUsername());
//        assertEquals(Role.DEFAULT.name(), user.getRole());
//
//        // Verify password was encoded with Base64
//        String expectedEncodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
//        assertEquals(expectedEncodedPassword, user.getPassword());
//
//        // Verify account status
//        assertFalse(user.isAccountLocked());
//        assertFalse(user.isCredentialsExpired());
//        assertTrue(user.isAccountActive());
//    }
//
//    @Test
//    void shouldNotCreateDefaultUserWhenOneAlreadyExists() {
//        // Given - an existing default user
//        BaseUser existingDefaultUser = BaseUser.of(
//                Username.from("existingDefault", new UsernamePolicy.DefaultUsernamePolicy()),
//                Role.DEFAULT,
//                "P@55w0rd",
//                UserAccountStatus.active()
//        );
//        userMap.put("existingDefault", existingDefaultUser);
//
//        String username = "newDefault";
//        String password = "P@55w0rd";
//
//        // When
//        service.ensureDefaultUserExists(username, password);
//
//        // Then - no new user should be created
//        Optional<BaseUser> newUser = queryPort.findByUsername(username);
//        assertFalse(newUser.isPresent(), "No new default user should be created");
//
//        // Verify existing user is unchanged
//        assertEquals(1, userMap.size());
//        assertTrue(userMap.containsKey("existingDefault"));
//    }
//
//    @Test
//    void shouldEncodePasswordUsingBase64() {
//        // Given
//        String password = "testPassword@123";
//
//        // When
//        String encoded = base64Encoder.encrypt(password);
//
//        // Then
//        String expected = Base64.getEncoder().encodeToString(password.getBytes());
//        assertEquals(expected, encoded);
//    }
//
//    @Test
//    void shouldSaveUserInMapRepository() {
//        // Given
//        BaseUser user = BaseUser.of(
//                Username.from("testUser", new UsernamePolicy.DefaultUsernamePolicy()),
//                Role.ADMIN,
//                "P@55w0rd",
//                UserAccountStatus.active()
//        );
//
//        // When
//        mapRepository.save(user);
//
//        // Then
//        assertEquals(1, userMap.size());
//        assertTrue(userMap.containsKey("testuser"));
//        assertEquals(user, userMap.get("testuser"));
//    }
//
//    // Implement the test ports using Base64 and Map
//    static class Base64PasswordEncoder implements PasswordEncoderPort {
//        @Override
//        public String encrypt(String plainText) {
//            return Base64.getEncoder().encodeToString(plainText.getBytes());
//        }
//    }
//
//    static class InMemoryMapUserRepository implements BaseUserRepositoryPort {
//        private final Map<String, BaseUser> userMap;
//
//        public InMemoryMapUserRepository(Map<String, BaseUser> userMap) {
//            this.userMap = userMap;
//        }
//
//
//        @Override
//        public void save(BaseUser user) {
//            userMap.put(user.getUsername(), user);
//        }
//    }
//
//    static class InMemoryMapUserQuery implements BaseUserQueryPort {
//        private final Map<String, BaseUser> userMap;
//
//        public InMemoryMapUserQuery(Map<String, BaseUser> userMap) {
//            this.userMap = userMap;
//        }
//
//        @Override
//        public boolean existsByRole(Role role) {
//            Collection<BaseUser> values = userMap.values();
//            return values.stream().anyMatch(user -> user.getRole().contains(role.name()));
//        }
//
//        @Override
//        public Optional<BaseUser> findByUsername(String username) {
//            return Optional.ofNullable(userMap.get(username));
//        }
//    }
//}
