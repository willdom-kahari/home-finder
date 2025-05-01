package com.waduclay.application.db;


import com.waduclay.homefinder.ports.UserRepository;
import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Component
@RequiredArgsConstructor
public class UserQueryAdapter implements com.waduclay.homefinder.ports.UserQuery {
    private final AppUserRepository userRepository;
    private final BaseUserRepository baseUserRepository;
    @Override
    public Optional<User> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public List<User> findByRole(Role role) {
        return List.of();
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
    public long count() {
        return 0;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }
}
