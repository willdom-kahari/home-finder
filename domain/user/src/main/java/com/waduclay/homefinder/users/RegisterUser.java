package com.waduclay.homefinder.users;

import com.waduclay.homefinder.enums.AuthenticationProvider;
import com.waduclay.homefinder.enums.Role;
import com.waduclay.homefinder.ports.*;
import com.waduclay.homefinder.shared.*;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Service handling user registration process
 *
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class RegisterUser {
    private final PasswordEncoderPort passwordEncoder;
    private final BaseUserRepositoryPort baseUserRepository;
    private final BaseUserQueryPort baseUserQueryPort;
    private final PasswordGeneratorPort passwordGenerator;
    private final UserRepositoryPort userRepository;
    private final UsernamePolicy usernamePolicy;
    private final static Logger log = Logger.getLogger(RegisterUser.class.getName());

    public RegisterUser(PasswordEncoderPort passwordEncoder,
                        BaseUserRepositoryPort baseUserRepository,
                        BaseUserQueryPort baseUserQueryPort,
                        PasswordGeneratorPort passwordGenerator,
                        UserRepositoryPort userRepository) {
        this(passwordEncoder, baseUserRepository, baseUserQueryPort,
                passwordGenerator, userRepository, UsernamePolicy.defaultUsernamePolicy());
    }

    // Additional constructor for testability (allows injecting custom username policy)
    public RegisterUser(PasswordEncoderPort passwordEncoder,
                        BaseUserRepositoryPort baseUserRepository,
                        BaseUserQueryPort baseUserQueryPort,
                        PasswordGeneratorPort passwordGenerator,
                        UserRepositoryPort userRepository,
                        UsernamePolicy usernamePolicy) {
        this.passwordEncoder = passwordEncoder;
        this.baseUserRepository = baseUserRepository;
        this.baseUserQueryPort = baseUserQueryPort;
        this.passwordGenerator = passwordGenerator;
        this.userRepository = userRepository;
        this.usernamePolicy = usernamePolicy;
    }

    public void execute(String username, String name, String surname,
                        String nationalId, String mobileNumber, String email,
                        Role role, AuthenticationProvider authenticationProvider) {
        validateUsernameNotExists(username);
        BaseUser baseUser = createBaseUser(username, role, authenticationProvider);
        User userDetails = createUserDetails(baseUser.getId(), name, surname, nationalId, mobileNumber, email);
        saveUserData(baseUser, userDetails);
    }

    private void validateUsernameNotExists(String username) {
        if (baseUserQueryPort.existsByUsername(username)) {
            throw new IllegalStateException("Username already exists");
        }
    }

    private BaseUser createBaseUser(String username, Role role, AuthenticationProvider authenticationProvider) {
        String password = passwordGenerator.generate();
        Username user = Username.of(username, usernamePolicy);
        Password pass = Password.of(password, passwordEncoder);

        return switch (role) {
            case DEFAULT -> throw new IllegalStateException("Default user already exists");
            case ADMIN -> BaseUser.createAdmin(user, pass);
            case USER -> BaseUser.createUser(user, pass, authenticationProvider);
        };
    }

    private User createUserDetails(UUID userId, String name, String surname,
                                   String nationalId, String mobileNumber, String email) {
        Name firstName = Name.from(name);
        Name lastName = Name.from(surname);
        NationalIdNumber nationalIdNumber = NationalIdNumber.from(nationalId);
        MobileNumber cell = MobileNumber.from(mobileNumber);
        Email presentEmail = email == null ? null : Email.from(email);
        return User.of(userId, nationalIdNumber, firstName, lastName, cell, presentEmail);
    }

    private void saveUserData(BaseUser baseUser, User userDetails) {
        baseUserRepository.save(baseUser);
        userRepository.save(userDetails);
    }

    public void ensureDefaultUserExists(String username, String password) {
        if (baseUserQueryPort.existsByRole(Role.DEFAULT)) {
            log.info("Default user already exists");
            return;
        }
        UsernamePolicy usernamePolicy = UsernamePolicy.configurableUsernamePolicy()
                .withAllowedCharacters("^[a-zA-Z0-9_.-]+$")
                .withLengthRange(4, 10)
                .build();

        Username defaultUsername = Username.of(username, usernamePolicy);
        Password defaultUserPassword = Password.of(password, passwordEncoder);

        BaseUser defaultUser = BaseUser.createDefaultUser(defaultUsername, defaultUserPassword);
        baseUserRepository.save(defaultUser);


    }
}
