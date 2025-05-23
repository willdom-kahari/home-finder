package com.waduclay.application.users.app;


import com.waduclay.application.users.base.BaseUser;
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
@Table(name = "app_users")
public class AppUser {
    @Id
    private UUID id;
    @MapsId
    @OneToOne(orphanRemoval = true, cascade = CascadeType.PERSIST)
    @JoinColumn
    private BaseUser baseUser;
    private String firstName;
    private String lastName;
    private String nationalId;
    private String email;
    private String mobileNumber;
    private String nationalIdUrl;
    private String currentPayslipUrl;

    public static AppUser of(User user) {
        return AppUser.builder()
                .baseUser(BaseUser.of(user))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .nationalId(user.getNationalIdNumber())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .build();
    }

    public User toUser() {
        return User.of(
                this.id,
                this.baseUser.getUsername(),
                this.baseUser.getPassword(),
                this.baseUser.getRole(),
                this.baseUser.getProvider(),
                this.baseUser.isActive(),
                this.baseUser.isAccountLocked(),
                this.baseUser.isCredentialsExpired(),
                this.baseUser.getFailedLoginAttempts()
        );
    }
}
