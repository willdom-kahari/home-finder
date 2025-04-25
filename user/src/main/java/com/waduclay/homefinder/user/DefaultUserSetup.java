package com.waduclay.homefinder.user;


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
        Username defaultUsername = Username.from(username);
        Password defaultUserPassword = Password.from(password);
        if (!queryPort.existsByRole(Role.DEFAULT)) {
            BaseUser defaultUser = BaseUser.createDefaultUser(defaultUsername, defaultUserPassword, passwordEncoder);
            repositoryPort.save(defaultUser);
        }

    }
}
