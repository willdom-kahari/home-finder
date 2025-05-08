package com.waduclay.application.users.service;

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
public class DefaultUserSetup implements ApplicationRunner {

    private final UserRegistrationService userRegistrationService;
    @Value("${default.user}")
    private String username;
    @Value("${default.password}")
    private String password;

    @Override
    public void run(ApplicationArguments args) {
        userRegistrationService.ensureDefaultUserExists(username, password);
    }
}
