package com.waduclay.application.security.events;

import com.waduclay.application.notification.NotificationCategory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.waduclay.application.notification.Notifier.sendSingleAlert;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogoutEventHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String email = authentication.getName();
        log.debug("User {} has successfully logged out.", email);
        sendSingleAlert(NotificationCategory.INFO, "You have logged out.", request, response);
        response.sendRedirect("/login");
    }
}
