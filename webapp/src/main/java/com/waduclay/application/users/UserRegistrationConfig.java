package com.waduclay.application.users;


import com.waduclay.homefinder.ports.*;
import com.waduclay.homefinder.users.services.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Configuration
@RequiredArgsConstructor
public class UserRegistrationConfig {
    private final EventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGenerator passwordGenerator;
    private final UserCommand userCommand;
    private final UserQuery userQuery;

    @Bean
    public UserRegistrationService userRegistrationService() {
        return new UserRegistrationService(
                passwordEncoder,
                userCommand,
                userQuery,
                passwordGenerator,
                eventPublisher
        );
    }
}
