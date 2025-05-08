package com.waduclay.application.security;


import com.waduclay.application.security.authentication.CustomAuthenticationEntryPoint;
import com.waduclay.application.security.events.LogoutEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final LogoutEventHandler logoutEventHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final RememberMeServices rememberMeServices;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request.requestMatchers("/css/**", "/js/**", "/register/**", "/resources/**", "/img/**").permitAll()
                                .requestMatchers("/webjars/**", "/error", "/", "/sign-in/**", "/login/**", "/favicon.ico").permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/apply/**", "/applications/**").hasAnyRole("ADMIN", "USER") //"/**/comments",
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint))
                .rememberMe(remember -> remember.rememberMeServices(rememberMeServices))
                .logout(logout -> logout
                        .logoutSuccessHandler(this.logoutEventHandler)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .permitAll()
                );

        return http.build();
    }
}
