package com.waduclay.application.db;

import com.waduclay.homefinder.ports.UserCommand;
import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Component
@RequiredArgsConstructor
public class UserCommandAdapter implements UserCommand {
    private final BaseUserRepository baseUserRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public User save(User user) {
        if (user.getRole().equals(Role.DEFAULT)) {
            BaseUser baseUser = BaseUser.of(user);
            baseUserRepository.save(baseUser);
            return user;
        }

        AppUser appUser = AppUser.of(user);
        appUserRepository.save(appUser);
        return user;
    }

    @Override
    public void delete(User user) {

    }
}
