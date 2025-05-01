package com.waduclay.application.db;


import com.waduclay.homefinder.ports.UserRepository;
import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final BaseUserRepository baseUserRepository;
    private final AppUserRepository appUserRepository;
    @Override
    public User save(User user) {
        if (user.getRole().equals(Role.DEFAULT)){
            BaseUser baseUser = BaseUser.of(user);
            baseUserRepository.save(baseUser);
            return user;
        }

        AppUser appUser = AppUser.of(user);
        appUserRepository.save(appUser);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public boolean existsByUsername(String username) {
        return baseUserRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByRole(Role role) {
        return baseUserRepository.existsByRole(role);
    }

    @Override
    public void delete(User user) {

    }
}
