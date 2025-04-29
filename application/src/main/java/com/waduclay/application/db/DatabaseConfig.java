package com.waduclay.application.db;


import com.waduclay.homefinder.ports.UserAggregateQuery;
import com.waduclay.homefinder.ports.UserAggregateRepository;
import com.waduclay.homefinder.users.UserAggregate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Configuration
public class DatabaseConfig {
    private final Map<String, UserAggregate> userMap = new HashMap<>();

    @Bean
    public UserAggregateQuery baseUserQuery(){
         return new InMemoryBaseUserQuery(userMap);
    }

    @Bean
    public UserAggregateRepository baseUserRepository(){
        return new InMemoryBaseUserRepositoryAdapter(userMap);
    }
}
