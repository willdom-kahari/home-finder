package com.waduclay.homefinder.ports;


import com.waduclay.homefinder.users.User;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public interface UserRepositoryPort {
    void save(User user);
}
