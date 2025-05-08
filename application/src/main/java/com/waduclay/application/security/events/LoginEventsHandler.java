package com.waduclay.application.security.events;



import com.waduclay.application.db.UserCommandAdapter;
import com.waduclay.application.db.UserQueryAdapter;
import com.waduclay.homefinder.ports.UserCommand;
import com.waduclay.homefinder.ports.UserQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LoginEventsHandler {
    private final UserQuery userQuery;
    private final UserCommand userCommand;

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        String username = success.getAuthentication().getName();
        userQuery.findAuthUserByName(username).ifPresent(user -> {
            user.resetLoginAttempt();
            userCommand.save(user);
        });

        log.info("User {} authenticated successfully.", username);
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent failures) {
        String username = failures.getAuthentication().getName();
        userQuery.findAuthUserByName(username).ifPresent(user -> {
            user.recordFailedLoginAttempt();
            userCommand.save(user);
        });

    }
}
