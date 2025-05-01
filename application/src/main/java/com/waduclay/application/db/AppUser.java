package com.waduclay.application.db;


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
}
