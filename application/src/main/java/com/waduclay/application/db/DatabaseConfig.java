package com.waduclay.application.db;

import com.waduclay.homefinder.ports.UserQuery;
import com.waduclay.homefinder.ports.UserRepository;
import com.waduclay.homefinder.users.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Configuration
public class DatabaseConfig {
    private final Map<String, User> userMap = new HashMap<>();

    @Bean
    public UserQuery baseUserQuery(){
         return new InMemoryBaseUserQuery(userMap);
    }

    @Bean
    public UserRepository baseUserRepository(){
        return new InMemoryBaseUserRepositoryAdapter(userMap);
    }
}
