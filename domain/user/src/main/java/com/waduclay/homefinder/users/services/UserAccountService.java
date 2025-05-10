package com.waduclay.homefinder.users.services;


import com.waduclay.homefinder.ports.UserCommand;
import com.waduclay.homefinder.ports.UserQuery;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class UserAccountService {
    private final UserCommand userCommand;
    private final UserQuery userQuery;

    public UserAccountService(UserCommand userCommand, UserQuery userQuery) {
        this.userCommand = userCommand;
        this.userQuery = userQuery;
    }

    public void resetLogin(String username) {
        userQuery.findAuthUserByName(username).ifPresent(user -> {
            user.resetLoginAttempt();
            userCommand.save(user);
        });
    }

    public void increaseLoginAttempt(String username) {
        userQuery.findAuthUserByName(username).ifPresent(user -> {
            user.recordFailedLoginAttempt();
            userCommand.save(user);
        });
    }
}
