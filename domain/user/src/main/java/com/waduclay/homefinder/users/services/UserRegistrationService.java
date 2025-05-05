package com.waduclay.homefinder.users.services;


import com.waduclay.homefinder.ports.*;
import com.waduclay.homefinder.shared.auth.Password;
import com.waduclay.homefinder.shared.auth.Username;
import com.waduclay.homefinder.shared.auth.enums.AuthenticationProvider;
import com.waduclay.homefinder.shared.auth.enums.Role;
import com.waduclay.homefinder.shared.exceptions.UserAlreadyExistsException;
import com.waduclay.homefinder.shared.exceptions.ValidationException;
import com.waduclay.homefinder.shared.personal.Email;
import com.waduclay.homefinder.shared.personal.MobileNumber;
import com.waduclay.homefinder.shared.personal.Name;
import com.waduclay.homefinder.shared.personal.NationalIdNumber;
import com.waduclay.homefinder.users.PersonalInformation;
import com.waduclay.homefinder.users.User;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRegistrationService {
    private static final Logger logger = Logger.getLogger(UserRegistrationService.class.getName());

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserQuery userQuery;
    private final PasswordGenerator passwordGenerator;
    private final UsernamePolicy usernamePolicy;
    private final EventPublisher eventPublisher;

    public UserRegistrationService(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            UserQuery userQuery,
            PasswordGenerator passwordGenerator,
            EventPublisher eventPublisher) {
        this(
                passwordEncoder,
                userRepository,
                userQuery,
                passwordGenerator,
                UsernamePolicy.defaultUsernamePolicy(),
                eventPublisher);
    }

    public UserRegistrationService(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            UserQuery userQuery,
            PasswordGenerator passwordGenerator,
            UsernamePolicy usernamePolicy,
            EventPublisher eventPublisher) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userQuery = userQuery;
        this.passwordGenerator = passwordGenerator;
        this.usernamePolicy = usernamePolicy;
        this.eventPublisher = eventPublisher;
    }

    public User registerUser(
            String username,
            String firstName,
            String lastName,
            String nationalId,
            String mobileNumber,
            String email,
            AuthenticationProvider authenticationProvider) {
        return processUserCreation(
                username,
                firstName,
                lastName,
                nationalId,
                mobileNumber,
                email,
                authenticationProvider,
                Role.USER,
                false);
    }

    public void ensureDefaultUserExists(String username, String password) {
        if (userQuery.existsByRole(Role.DEFAULT)) {
            logger.fine("Default user already exists");
            return;
        }

        Username usernameObj = Username.of(username, UsernamePolicy.configurableUsernamePolicy()
                .withAllowedCharacters("^[a-zA-Z0-9_.-]+$")
                .withLengthRange(4, 10)
                .build());
        Password passwordObj = Password.of(password, passwordEncoder);

        User defaultUser = User.createDefaultUser(usernameObj, passwordObj);
        saveAndPublishEvents(defaultUser);
        logger.config("Default user created successfully");
    }

    public User createAdminUser(
            String username,
            String firstName,
            String lastName,
            String nationalId,
            String mobileNumber,
            String email) {
        return processUserCreation(
                username,
                firstName,
                lastName,
                nationalId,
                mobileNumber,
                email,
                null,
                Role.ADMIN,
                true);
    }


    private User processUserCreation(
            String username,
            String firstName,
            String lastName,
            String nationalId,
            String mobileNumber,
            String email,
            AuthenticationProvider authenticationProvider,
            Role role,
            boolean isAdmin) {

        validateInputs(username, firstName, lastName, nationalId, mobileNumber);
        ensureUsernameDoesNotExist(username);

        try {
            Username usernameObj = Username.of(username, usernamePolicy);
            Password passwordObj = Password.of(passwordGenerator.generate(), passwordEncoder);
            PersonalInformation personalInfo = new PersonalInformation(
                    NationalIdNumber.from(nationalId),
                    Name.from(firstName),
                    Name.from(lastName),
                    MobileNumber.from(mobileNumber),
                    email == null ? null : Email.from(email)
            );

            User user = createUserAggregate(usernameObj, passwordObj, authenticationProvider, role, personalInfo, isAdmin);
            saveAndPublishEvents(user);

            logger.log(Level.FINE, "{0} user created successfully: {1}", new Object[]{role.name(), username});
            return user;
        } catch (Exception e) {
            handleException(e, username);
            throw e;
        }
    }

    private User createUserAggregate(
            Username username,
            Password password,
            AuthenticationProvider authenticationProvider,
            Role role,
            PersonalInformation personalInfo,
            boolean isAdmin) {
        if (isAdmin) {
            User adminUser = User.createAdmin(username, password);
            adminUser.updatePersonalInformation(
                    personalInfo.nationalIdNumber(),
                    personalInfo.firstName(),
                    personalInfo.lastName(),
                    personalInfo.mobileNumber(),
                    personalInfo.email());
            return adminUser;
        } else {
            return User.createUser(
                    username,
                    password,
                    authenticationProvider,
                    personalInfo.nationalIdNumber(),
                    personalInfo.firstName(),
                    personalInfo.lastName(),
                    personalInfo.mobileNumber(),
                    personalInfo.email());
        }
    }

    private void validateInputs(String username, String firstName, String lastName, String nationalId, String mobileNumber) {
        if (isNullOrEmpty(username)) throw new ValidationException("Username cannot be empty");
        if (isNullOrEmpty(firstName)) throw new ValidationException("First name cannot be empty");
        if (isNullOrEmpty(lastName)) throw new ValidationException("Last name cannot be empty");
        if (isNullOrEmpty(nationalId)) throw new ValidationException("National ID cannot be empty");
        if (isNullOrEmpty(mobileNumber)) throw new ValidationException("Mobile number cannot be empty");
    }

    private void ensureUsernameDoesNotExist(String username) {
        if (userQuery.existsByUsername(username)) {
            logger.log(Level.WARNING, "Username already exists: {0}", username);
            throw new UserAlreadyExistsException("Username already exists: " + username);
        }
    }

    private void saveAndPublishEvents(User user) {
        User savedUser = userRepository.save(user);
        eventPublisher.publishAll(savedUser.getDomainEvents());
    }

    private void handleException(Exception e, String username) {
        if (e instanceof IllegalArgumentException) {
            logger.log(Level.WARNING, "Validation error during registration for username {0}: {1}",
                    new Object[]{username, e.getMessage()});
        } else {
            logger.log(Level.SEVERE, "Error during registration for username " + username, e);
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
