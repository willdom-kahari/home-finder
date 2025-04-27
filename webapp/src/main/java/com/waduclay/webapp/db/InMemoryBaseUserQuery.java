package com.waduclay.webapp.db;


import com.waduclay.homefinder.baseuser.BaseUser;
import com.waduclay.homefinder.baseuser.BaseUserQueryPort;
import com.waduclay.homefinder.shared.Role;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */

public class InMemoryBaseUserQuery implements BaseUserQueryPort {
    private final Map<String, BaseUser> userMap;

    public InMemoryBaseUserQuery(Map<String, BaseUser> userMap) {
        this.userMap = userMap;
    }

    @Override
    public boolean existsByRole(Role role) {
        Collection<BaseUser> values = userMap.values();
        return values.stream().anyMatch(user -> user.getRole().contains(role.name()));
    }

    @Override
    public Optional<BaseUser> findByUsername(String username) {
        return Optional.ofNullable(userMap.get(username));
    }

    @Override
    public boolean existsByUsername(String username) {
        return Optional.ofNullable(userMap.get(username)).isPresent();
    }
}
