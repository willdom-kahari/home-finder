package com.waduclay.application.db;




import com.waduclay.homefinder.ports.UserQuery;
import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.users.User;

import java.util.*;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */

public class InMemoryBaseUserQuery implements UserQuery {
    private final Map<String, User> userMap;

    public InMemoryBaseUserQuery(Map<String, User> userMap) {
        this.userMap = userMap;
    }

    @Override
    public boolean existsByRole(Role role) {
        Collection<User> values = userMap.values();
        return values.stream().anyMatch(user -> user.getRole().contains(role.name()));
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userMap.get(username));
    }

    @Override
    public List<User> findByRole(Role role) {
        return List.of();
    }

    @Override
    public boolean existsByUsername(String username) {
        return Optional.ofNullable(userMap.get(username)).isPresent();
    }
}
