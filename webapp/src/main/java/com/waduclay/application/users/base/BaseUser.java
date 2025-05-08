package com.waduclay.application.users.base;


import com.waduclay.homefinder.shared.auth.enums.AuthenticationProvider;
import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "base_users")
public class BaseUser {
    @Id
    private UUID id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private AuthenticationProvider provider;
    private boolean active;
    private boolean accountLocked;
    private boolean credentialsExpired;
    private int failedLoginAttempts;

    public static BaseUser of(User user) {
        return BaseUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .provider(user.getAuthenticationProvider())
                .active(user.isAccountActive())
                .accountLocked(user.isAccountLocked())
                .credentialsExpired(user.isCredentialsExpired())
                .failedLoginAttempts(user.getFailedLoginAttempts())
                .build();
    }
}
