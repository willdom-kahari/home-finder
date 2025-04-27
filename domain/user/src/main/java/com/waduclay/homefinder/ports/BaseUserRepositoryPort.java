package com.waduclay.homefinder.ports;


import com.waduclay.homefinder.users.BaseUser;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public interface BaseUserRepositoryPort {
    void save(BaseUser user);
}
