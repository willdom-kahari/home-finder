package com.waduclay.application.security.authentication;


import com.waduclay.application.notification.NotificationCategory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static com.waduclay.application.notification.FlashNotification.sendSingleAlert;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Configuration
// Redirects user to the login page if they try to access protected resources
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        sendSingleAlert(NotificationCategory.INFO, "Please login!", request, response);
        response.sendRedirect("/login");
    }
}
