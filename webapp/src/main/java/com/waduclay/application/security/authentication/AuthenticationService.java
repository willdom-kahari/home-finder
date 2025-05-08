package com.waduclay.application.security.authentication;

import com.waduclay.application.notification.NotificationCategory;
import com.waduclay.application.security.SecurityUser;
import com.waduclay.application.utils.UrlConstants;
import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.users.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

import static com.waduclay.application.notification.FlashNotification.sendSingleAlert;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Map<Role, String> REDIRECTION_URLS = new EnumMap<>(Role.class);

    static {
        REDIRECTION_URLS.put(Role.DEFAULT, UrlConstants.REDIRECT_TO_DEFAULT_USER_DASHBOARD);
        REDIRECTION_URLS.put(Role.USER, UrlConstants.REDIRECT_CLIENT_VIEW_POSTS);
        REDIRECTION_URLS.put(Role.ADMIN, UrlConstants.REDIRECT_TO_ADMIN_POSTS);
    }

    private final AuthenticationManager authenticationManager;
    private final RememberMeServices rememberMeServices;
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();
    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();

    public String authenticate(AuthenticationRequest authenticationRequest,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        try {
            Authentication authentication = attemptAuthentication(authenticationRequest);
            setupSecurityContext(authentication, request, response);
            return determineRedirectUrl(authentication);
        } catch (AuthenticationException e) {
            handleAuthenticationFailure(e, request, response);
            return UrlConstants.REDIRECT_LOGIN;
        }
    }

    private Authentication attemptAuthentication(AuthenticationRequest authenticationRequest)
            throws AuthenticationException {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
    }

    private void setupSecurityContext(Authentication authentication,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
        rememberMeServices.loginSuccess(request, response, authentication);
    }

    private String determineRedirectUrl(Authentication authentication) {
        User user = ((SecurityUser) authentication.getPrincipal()).user();
        return REDIRECTION_URLS.getOrDefault(user.getRole(), UrlConstants.REDIRECT_TO_ADMIN_POSTS);
    }

    private void handleAuthenticationFailure(AuthenticationException e,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        log.warn("Authentication failed: {}", e.getMessage());
        sendSingleAlert(NotificationCategory.ERROR, e.getMessage(), request, response);
    }
}
