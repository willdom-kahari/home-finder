package com.waduclay.application.config;

import com.waduclay.application.db.InMemoryBaseUserQuery;
import com.waduclay.application.db.InMemoryBaseUserRepositoryAdapter;
import com.waduclay.application.security.PasswordEncoderAdapter;
import com.waduclay.homefinder.users.UserAggregate;
import com.waduclay.homefinder.users.services.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationSetup implements ApplicationRunner {
    private final ApplicationEventPublisherAdapter applicationEventPublisherAdapter;
    private final Map<String, UserAggregate> userMap = new HashMap<>();
    private final PasswordEncoderAdapter passwordEncoderAdapter;
    private final PasswordGeneratorAdapter passwordGeneratorAdapter;
    private final InMemoryBaseUserRepositoryAdapter baseUserRepository = new InMemoryBaseUserRepositoryAdapter(userMap);
    private final InMemoryBaseUserQuery baseUserQuery = new InMemoryBaseUserQuery(userMap);
    @Value("${default.user}")
    private String username;
    @Value("${default.password}")
    private String password;

    @Override
    public void run(ApplicationArguments args) {
        UserRegistrationService defaultUserSetup = new UserRegistrationService(
                passwordEncoderAdapter,
                baseUserRepository,
                baseUserQuery,
                passwordGeneratorAdapter,
                applicationEventPublisherAdapter
        );
        defaultUserSetup.ensureDefaultUserExists(username, password);
        defaultUserSetup.ensureDefaultUserExists(username, password);
        System.out.println("userMap = " + userMap.get(username));
    }
}
