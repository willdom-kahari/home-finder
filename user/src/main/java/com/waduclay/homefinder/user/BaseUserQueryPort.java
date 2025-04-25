package com.waduclay.homefinder.user;


import java.util.Optional;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public interface BaseUserQueryPort {
    boolean existsByRole(Role role);

    Optional<BaseUser> findByUsername(String username);
}
