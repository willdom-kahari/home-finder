package com.waduclay.application.db;




import com.waduclay.homefinder.ports.UserRepository;
import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.users.User;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class InMemoryBaseUserRepositoryAdapter implements UserRepository {
    private final Map<String, User> userMap;

    public InMemoryBaseUserRepositoryAdapter(Map<String, User> userMap) {
        this.userMap = userMap;
    }


    @Override
    public User save(User user) {
        userMap.put(user.getUsername(), user);
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
        return false;
    }

    @Override
    public boolean existsByRole(Role role) {
        return false;
    }

    @Override
    public void delete(User User) {

    }
}
