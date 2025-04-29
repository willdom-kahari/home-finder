package com.waduclay.application.db;


import com.waduclay.homefinder.enums.Role;
import com.waduclay.homefinder.ports.UserAggregateQuery;
import com.waduclay.homefinder.users.UserAggregate;

import java.util.*;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */

public class InMemoryBaseUserQuery implements UserAggregateQuery {
    private final Map<String, UserAggregate> userMap;

    public InMemoryBaseUserQuery(Map<String, UserAggregate> userMap) {
        this.userMap = userMap;
    }

    @Override
    public boolean existsByRole(Role role) {
        Collection<UserAggregate> values = userMap.values();
        return values.stream().anyMatch(user -> user.getRole().contains(role.name()));
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<UserAggregate> findAll() {
        return List.of();
    }

    @Override
    public Optional<UserAggregate> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserAggregate> findByUsername(String username) {
        return Optional.ofNullable(userMap.get(username));
    }

    @Override
    public List<UserAggregate> findByRole(Role role) {
        return List.of();
    }

    @Override
    public boolean existsByUsername(String username) {
        return Optional.ofNullable(userMap.get(username)).isPresent();
    }
}
