package com.waduclay.application;

import com.waduclay.application.db.AppUser;
import com.waduclay.application.db.AppUserRepository;
import com.waduclay.application.db.BaseUser;
import com.waduclay.application.db.BaseUserRepository;
import com.waduclay.homefinder.ports.UserQuery;
import com.waduclay.homefinder.users.User;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    ApplicationRunner applicationRunner(UserQuery repository) {
//
//        Optional<User> baseUser = repository.findAuthUserByName("root");
//        System.out.println("hi");
//        return args -> {
//
//        };
//    }
}
