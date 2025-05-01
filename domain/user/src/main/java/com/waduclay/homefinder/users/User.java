package com.waduclay.homefinder.users;


import com.waduclay.homefinder.shared.DomainEvent;
import com.waduclay.homefinder.shared.*;
import com.waduclay.homefinder.shared.auth.Password;
import com.waduclay.homefinder.shared.auth.UserAccountStatus;
import com.waduclay.homefinder.shared.auth.Username;
import com.waduclay.homefinder.shared.auth.enums.AuthenticationProvider;
import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.shared.personal.*;
import com.waduclay.homefinder.users.events.*;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * UserAggregate serves as the aggregate root for the User domain.
 * It combines authentication information (previously in BaseUser) with
 * personal information (previously in User) to create a cohesive entity.
 */
public class User extends Entity<UUID> {
    // Core fields
    private final Username username;
    private final Role role;
    private final AuthenticationProvider authenticationProvider;

    // Mutable fields
    private Password password;
    private UserAccountStatus userAccountStatus;
    private int failedLoginAttempts;

    // Personal information
    private NationalIdNumber nationalIdNumber;
    private Name firstName;
    private Name lastName;
    private MobileNumber mobileNumber;
    private Email email;
    private Url nationalIdUrl;
    private Url currentPayslipUrl;

    // Domain events
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private User(
            UUID id,
            Username username,
            Role role,
            Password password,
            UserAccountStatus userAccountStatus,
            AuthenticationProvider authenticationProvider,
            PersonalInformation personalInfo) {
        super(id);
        this.username = InputGuard.requireNonNull(username, "username");
        this.role = InputGuard.requireNonNull(role, "role");
        this.password = InputGuard.requireNonNull(password, "password");
        this.userAccountStatus = userAccountStatus != null ? userAccountStatus : UserAccountStatus.inactive();
        this.authenticationProvider = InputGuard.requireNonNull(authenticationProvider, "authentication provider");

        // Initialise personal information
        if (personalInfo != null) {
            this.nationalIdNumber = personalInfo.nationalIdNumber();
            this.firstName = personalInfo.firstName();
            this.lastName = personalInfo.lastName();
            this.mobileNumber = personalInfo.mobileNumber();
            this.email = personalInfo.email();
        }
    }

    /**
     * Centralised factory method for creating users.
     */
    private static User create(
            Username username,
            Role role,
            Password password,
            UserAccountStatus accountStatus,
            AuthenticationProvider authenticationProvider,
            PersonalInformation personalInfo) {
        User user = new User(
                UUID.randomUUID(),
                username,
                role,
                password,
                accountStatus,
                authenticationProvider,
                personalInfo
        );
        user.addDomainEvent(new UserCreatedEvent(user.getId(), username.getValue()));
        return user;
    }

    /**
     * Factory method to create a default user.
     */
    public static User createDefaultUser(Username username, Password password) {
        return create(username, Role.DEFAULT, password, UserAccountStatus.active(), AuthenticationProvider.APP, null);
    }

    /**
     * Factory method to create an admin user.
     */
    public static User createAdmin(Username username, Password password) {
        return create(username, Role.ADMIN, password, UserAccountStatus.active(), AuthenticationProvider.APP, null);
    }

    /**
     * Factory method to create a regular user.
     */
    public static User createUser(
            Username username,
            Password password,
            AuthenticationProvider authenticationProvider,
            NationalIdNumber nationalIdNumber,
            Name firstName,
            Name lastName,
            MobileNumber mobileNumber,
            Email email
    ) {
        PersonalInformation personalInfo = new PersonalInformation(
                nationalIdNumber,
                firstName,
                lastName,
                mobileNumber,
                email
        );

        return create(
                username,
                Role.USER,
                password,
                UserAccountStatus.inactive(),
                authenticationProvider,
                personalInfo
        );
    }

    /**
     * Updates the user's personal information.
     */
    public void updatePersonalInformation(
            NationalIdNumber nationalIdNumber,
            Name firstName,
            Name lastName,
            MobileNumber mobileNumber,
            Email email) {
        this.nationalIdNumber = nationalIdNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.email = email;

        addDomainEvent(new UserUpdatedEvent(getId(), getUsername()));
    }

    /**
     * Handles failed login attempts, locking the account if the maximum is reached.
     */
    public void recordFailedLoginAttempt() {
        final int MAX_FAILED_ATTEMPTS = 3;

        if (failedLoginAttempts < MAX_FAILED_ATTEMPTS) {
            failedLoginAttempts++;
            if (failedLoginAttempts == MAX_FAILED_ATTEMPTS) {
                userAccountStatus = UserAccountStatus.locked();
                addDomainEvent(new AccountLockedEvent(getId(), getUsername()));
            }
        }
    }

    /**
     * Resets the login attempt counter.
     */
    public void resetLoginAttempt() {
        failedLoginAttempts = 0;
    }

    /**
     * Unlocks the user account.
     */
    public void unlockAccount() {
        userAccountStatus = UserAccountStatus.active();
        resetLoginAttempt();
        addDomainEvent(new AccountUnlockedEvent(getId(), getUsername()));
    }

    /**
     * Changes the user's password.
     */
    public void changePassword(Password newPassword) {
        this.password = newPassword;
        userAccountStatus = UserAccountStatus.active();
        addDomainEvent(new PasswordChangedEvent(getId(), getUsername()));
    }

    /**
     * Adds a national ID URL if not already added.
     */
    public void addNationalIdUrl(Url nationalIdUrl) {
        if (this.nationalIdUrl != null) {
            throw new IllegalStateException("National ID URL already exists.");
        }
        this.nationalIdUrl = nationalIdUrl;
    }

    /**
     * Adds a payslip URL if not already added.
     */
    public void addPayslipUrl(Url payslipUrl) {
        if (this.currentPayslipUrl != null) {
            throw new IllegalStateException("Payslip URL already exists.");
        }
        this.currentPayslipUrl = payslipUrl;
    }

    private void addDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        List<DomainEvent> events = Collections.unmodifiableList(domainEvents);
        domainEvents.clear();
        return events;
    }

    // Getters
    public String getUsername() {
        return username.getValue();
    }

    public Role getRole() {
        return role;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public boolean isCredentialsExpired() {
        return userAccountStatus.isCredentialsExpired();
    }

    public boolean isAccountLocked() {
        return userAccountStatus.isAccountLocked();
    }

    public boolean isAccountActive() {
        return userAccountStatus.isAccountActive();
    }

    // Personal information getters
    public String getFirstName() {
        return firstName != null ? firstName.getValue() : null;
    }

    public String getLastName() {
        return lastName != null ? lastName.getValue() : null;
    }

    public String getMobileNumber() {
        return mobileNumber != null ? mobileNumber.getValue() : null;
    }

    public String getEmail() {
        return email != null ? email.getValue() : null;
    }

    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }
}

