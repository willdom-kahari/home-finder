package com.waduclay.webapp.config;


import com.waduclay.homefinder.baseuser.BaseUser;
import com.waduclay.homefinder.baseuser.DefaultUserSetup;
import com.waduclay.webapp.db.InMemoryBaseUserQuery;
import com.waduclay.webapp.db.InMemoryBaseUserRepositoryAdapter;
import com.waduclay.webapp.security.PasswordEncoderAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationSetup implements ApplicationRunner {
    private final Map<String, BaseUser> userMap = new HashMap<>();
    private final PasswordEncoderAdapter passwordEncoderAdapter;
    private final InMemoryBaseUserRepositoryAdapter baseUserRepository = new InMemoryBaseUserRepositoryAdapter(userMap);
    private final InMemoryBaseUserQuery baseUserQuery = new InMemoryBaseUserQuery(userMap);
    @Value("${default.user}")
    private String username;
    @Value("${default.password}")
    private String password;

    @Override
    public void run(ApplicationArguments args) {
        DefaultUserSetup defaultUserSetup = new DefaultUserSetup(
                passwordEncoderAdapter, baseUserRepository, baseUserQuery
        );
        defaultUserSetup.ensureDefaultUserExists(username, password);
        defaultUserSetup.ensureDefaultUserExists(username, password);

        log.info(userMap.toString());
    }
}
