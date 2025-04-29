package com.waduclay.application.db;


import com.waduclay.homefinder.enums.Role;
import com.waduclay.homefinder.ports.UserAggregateRepository;
import com.waduclay.homefinder.users.UserAggregate;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class InMemoryBaseUserRepositoryAdapter implements UserAggregateRepository {
    private final Map<String, UserAggregate> userMap;

    public InMemoryBaseUserRepositoryAdapter(Map<String, UserAggregate> userMap) {
        this.userMap = userMap;
    }


    @Override
    public UserAggregate save(UserAggregate user) {
        userMap.put(user.getUsername(), user);
        return user;
    }



    @Override
    public Optional<UserAggregate> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<UserAggregate> findByUsername(String username) {
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
    public void delete(UserAggregate userAggregate) {

    }
}
