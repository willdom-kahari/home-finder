package com.waduclay.homefinder.users;


import com.waduclay.homefinder.enums.Role;
import com.waduclay.homefinder.ports.BaseUserQueryPort;
import com.waduclay.homefinder.ports.BaseUserRepositoryPort;
import com.waduclay.homefinder.ports.PasswordEncoderPort;
import com.waduclay.homefinder.ports.UsernamePolicy;
import com.waduclay.homefinder.shared.Password;
import com.waduclay.homefinder.shared.Username;

import java.util.logging.Logger;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class DefaultUserSetup {
    private final static Logger log = Logger.getLogger(DefaultUserSetup.class.getName());
    private final PasswordEncoderPort passwordEncoder;
    private final BaseUserRepositoryPort repositoryPort;
    private final BaseUserQueryPort queryPort;

    public DefaultUserSetup(PasswordEncoderPort passwordEncoder, BaseUserRepositoryPort repositoryPort, BaseUserQueryPort queryPort) {
        this.passwordEncoder = passwordEncoder;
        this.repositoryPort = repositoryPort;
        this.queryPort = queryPort;
    }

    public void ensureDefaultUserExists(String username, String password) {
        if (queryPort.existsByRole(Role.DEFAULT)) {
            log.info("Default user already exists");
            return;
        }
        UsernamePolicy usernamePolicy = UsernamePolicy.configurableUsernamePolicy()
                .withAllowedCharacters("^[a-zA-Z0-9_.-]+$")
                .withLengthRange(4, 10)
                .build();

        Username defaultUsername = Username.of(username, usernamePolicy);
        Password defaultUserPassword = Password.of(password, passwordEncoder);

        BaseUser defaultUser = BaseUser.createDefaultUser(defaultUsername, defaultUserPassword);
        repositoryPort.save(defaultUser);


    }
}
