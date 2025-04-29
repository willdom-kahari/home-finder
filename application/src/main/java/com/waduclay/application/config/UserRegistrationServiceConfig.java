package com.waduclay.application.config;


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
public class UserRegistrationServiceConfig {
    private final EventPublisher eventPublisherAdapter;
    private final PasswordEncoderPort passwordEncoderAdapter;
    private final PasswordGeneratorPort passwordGeneratorAdapter;
    private final UserAggregateRepository baseUserRepository;
    private final UserAggregateQuery baseUserQuery;

    @Bean
    public UserRegistrationService userRegistrationService() {
        return new UserRegistrationService(
                passwordEncoderAdapter,
                baseUserRepository,
                baseUserQuery,
                passwordGeneratorAdapter,
                eventPublisherAdapter
        );
    }
}
