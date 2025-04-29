package com.waduclay.homefinder.users.services;

import com.waduclay.homefinder.enums.AuthenticationProvider;
import com.waduclay.homefinder.enums.Role;
import com.waduclay.homefinder.ports.*;
import com.waduclay.homefinder.shared.*;
import com.waduclay.homefinder.users.PersonalInformation;
import com.waduclay.homefinder.users.UserAggregate;
import com.waduclay.homefinder.users.exceptions.UserAlreadyExistsException;
import com.waduclay.homefinder.users.exceptions.ValidationException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRegistrationService {
    private static final Logger logger = Logger.getLogger(UserRegistrationService.class.getName());

    private final PasswordEncoderPort passwordEncoder;
    private final UserAggregateRepository userRepository;
    private final UserAggregateQuery userQuery;
    private final PasswordGeneratorPort passwordGenerator;
    private final UsernamePolicy usernamePolicy;
    private final EventPublisher eventPublisher;

    public UserRegistrationService(
            PasswordEncoderPort passwordEncoder,
            UserAggregateRepository userRepository,
            UserAggregateQuery userQuery,
            PasswordGeneratorPort passwordGenerator,
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
            PasswordEncoderPort passwordEncoder,
            UserAggregateRepository userRepository,
            UserAggregateQuery userQuery,
            PasswordGeneratorPort passwordGenerator,
            UsernamePolicy usernamePolicy,
            EventPublisher eventPublisher) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userQuery = userQuery;
        this.passwordGenerator = passwordGenerator;
        this.usernamePolicy = usernamePolicy;
        this.eventPublisher = eventPublisher;
    }

    public UserAggregate registerUser(
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
            logger.info("Default user already exists");
            return;
        }

        Username usernameObj = Username.of(username, UsernamePolicy.configurableUsernamePolicy()
                .withAllowedCharacters("^[a-zA-Z0-9_.-]+$")
                .withLengthRange(4, 10)
                .build());
        Password passwordObj = Password.of(password, passwordEncoder);

        UserAggregate defaultUser = UserAggregate.createDefaultUser(usernameObj, passwordObj);
        saveAndPublishEvents(defaultUser);
        logger.info("Default user created successfully");
    }

    public UserAggregate createAdminUser(
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

    private UserAggregate processUserCreation(
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

            UserAggregate userAggregate = createUserAggregate(usernameObj, passwordObj, authenticationProvider, role, personalInfo, isAdmin);
            saveAndPublishEvents(userAggregate);

            logger.log(Level.INFO, "{0} user created successfully: {1}", new Object[]{role.name(), username});
            return userAggregate;
        } catch (Exception e) {
            handleException(e, username);
            throw e;
        }
    }

    private UserAggregate createUserAggregate(
            Username username,
            Password password,
            AuthenticationProvider authenticationProvider,
            Role role,
            PersonalInformation personalInfo,
            boolean isAdmin) {
        if (isAdmin) {
            UserAggregate adminUser = UserAggregate.createAdmin(username, password);
            adminUser.updatePersonalInformation(
                    personalInfo.nationalIdNumber(),
                    personalInfo.firstName(),
                    personalInfo.lastName(),
                    personalInfo.mobileNumber(),
                    personalInfo.email());
            return adminUser;
        } else {
            return UserAggregate.createUser(
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

    private void saveAndPublishEvents(UserAggregate userAggregate) {
        UserAggregate savedUser = userRepository.save(userAggregate);
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
