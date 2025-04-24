package com.waduclay.homefinder.user;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public interface BaseUserRepositoryPort {
    boolean existsByRole(Role role);
    void save(BaseUser user);
}
