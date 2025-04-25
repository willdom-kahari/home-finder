package com.waduclay.homefinder.user;


import java.util.Optional;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public interface BaseUserRepositoryPort {
    boolean existsByRole(Role role);

    Optional<BaseUser> findByUsername(String username);

    void save(BaseUser user);
}
