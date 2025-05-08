package com.waduclay.application.users.service;


import com.waduclay.application.users.app.AppUser;
import com.waduclay.application.users.app.AppUserRepository;
import com.waduclay.application.users.base.BaseUser;
import com.waduclay.application.users.base.BaseUserRepository;
import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public Optional<User> findAuthUserByName(String name) {
        final BaseUser baseUser = baseUserRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        final AppUser appUser = AppUser.builder()
                .id(baseUser.getId())
                .baseUser(baseUser)
                .build();
        return Optional.ofNullable(appUser)
                .map(AppUser::toUser);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByBaseUser_Username(username)
                .map(AppUser::toUser);
    }

    @Override
    public List<User> findByRole(Role role) {
        if (role.equals(Role.DEFAULT)) {
            return List.of();
        }
        return userRepository.findAppUsersByBaseUser_Role(role)
                .stream()
                .map(AppUser::toUser)
                .toList();
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
        return userRepository.findAll().stream()
                .map(AppUser::toUser)
                .toList();
    }
}
