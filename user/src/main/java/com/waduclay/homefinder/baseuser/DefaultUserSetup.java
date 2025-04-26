package com.waduclay.homefinder.baseuser;


import com.waduclay.homefinder.shared.*;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class DefaultUserSetup {
    private final PasswordEncoderPort passwordEncoder;
    private final BaseUserRepositoryPort repositoryPort;
    private final BaseUserQueryPort queryPort;

    public DefaultUserSetup(PasswordEncoderPort passwordEncoder, BaseUserRepositoryPort repositoryPort, BaseUserQueryPort queryPort) {
        this.passwordEncoder = passwordEncoder;
        this.repositoryPort = repositoryPort;
        this.queryPort = queryPort;
    }

    public void ensureDefaultUserExists(String username, String password) {
        UsernamePolicy usernamePolicy = UsernamePolicy.configurableUsernamePolicy()
                .withAllowedCharacters("^[a-zA-Z0-9_.-]+$")
                .withLengthRange(4, 10)
                .build();

        Username defaultUsername = Username.of(username, usernamePolicy);
        Password defaultUserPassword = Password.of(password, passwordEncoder);
        if (!queryPort.existsByRole(Role.DEFAULT)) {
            BaseUser defaultUser = BaseUser.createDefaultUser(defaultUsername, defaultUserPassword);
            repositoryPort.save(defaultUser);
        }

    }
}
