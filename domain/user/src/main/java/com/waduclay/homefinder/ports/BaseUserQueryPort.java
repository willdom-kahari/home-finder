package com.waduclay.homefinder.ports;


import com.waduclay.homefinder.enums.Role;
import com.waduclay.homefinder.users.BaseUser;

import java.util.Optional;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public interface BaseUserQueryPort {
    boolean existsByRole(Role role);

    Optional<BaseUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
