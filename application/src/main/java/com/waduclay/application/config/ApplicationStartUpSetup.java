package com.waduclay.application.config;

import com.waduclay.homefinder.users.services.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartUpSetup implements ApplicationRunner {

    @Value("${default.user}")
    private String username;
    @Value("${default.password}")
    private String password;
    private final UserRegistrationService userRegistrationService;

    @Override
    public void run(ApplicationArguments args) {
        userRegistrationService.ensureDefaultUserExists(username, password);
    }
}
