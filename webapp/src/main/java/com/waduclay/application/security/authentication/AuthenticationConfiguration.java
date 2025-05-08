package com.waduclay.application.security.authentication;

import com.waduclay.application.security.JpaUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfiguration {
    private final ApplicationEventPublisher applicationEventPublisher;
    @Value("${remember-me.key}")
    private String rememberMeKey;

    // Encode password in the database
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(JpaUserDetailsService userDetailsService) {
        // Allow remember me service.
        RememberMeAuthenticationProvider rememberMeAuthenticationProvider = new RememberMeAuthenticationProvider(rememberMeKey);
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        ProviderManager providerManager = new ProviderManager(authProvider, rememberMeAuthenticationProvider);
        /*
         *  The applicationEventPublisher is used to publish events related to authentication.
         * It publishes either a AuthenticationSuccessEvent or AuthenticationFailureEvent depending
         * on the authentication. Only set when using a custom authentication manager bean.
         */
        providerManager.setAuthenticationEventPublisher(authenticationEventPublisher());

        return providerManager;
    }

    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    @Bean
    RememberMeAuthenticationFilter rememberMeFilter(AuthenticationManager authenticationManager,
                                                    RememberMeServices rememberMeServices) {
        return new RememberMeAuthenticationFilter(authenticationManager, rememberMeServices);
    }

    @Bean
    RememberMeServices rememberMeServices(JpaUserDetailsService userDetailsService) {
        return new TokenBasedRememberMeServices(rememberMeKey, userDetailsService);
    }

}
