package com.waduclay.homefinder.user;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class DefaultUserSetup {
    private final PasswordEncoderPort passwordEncoder;
    private final BaseUserRepositoryPort repository;

    public DefaultUserSetup(PasswordEncoderPort passwordEncoder, BaseUserRepositoryPort repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    public void ensureDefaultUserExists(String username, String password) {
        Username defaultUsername = Username.from(username);
        Password defaultUserPassword = Password.from(password);
        if (!repository.existsByRole(Role.DEFAULT)) {
            BaseUser defaultUser = BaseUser.createDefaultUser(defaultUsername, defaultUserPassword, passwordEncoder);
            repository.save(defaultUser);
        }

    }
}
