package com.waduclay.homefinder.genericuser;

import com.waduclay.homefinder.baseuser.AuthenticationProvider;
import com.waduclay.homefinder.baseuser.BaseUser;
import com.waduclay.homefinder.baseuser.BaseUserQueryPort;
import com.waduclay.homefinder.baseuser.BaseUserRepositoryPort;
import com.waduclay.homefinder.shared.*;

import java.util.UUID;

/**
 * Service handling user registration process
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class UserRegistrationService {
    private final PasswordEncoderPort passwordEncoder;
    private final BaseUserRepositoryPort baseUserRepository;
    private final BaseUserQueryPort baseUserQueryPort;
    private final PasswordGeneratorPort passwordGenerator;
    private final UserRepositoryPort userRepository;
    private final UsernamePolicy usernamePolicy;

    public UserRegistrationService(PasswordEncoderPort passwordEncoder,
                                   BaseUserRepositoryPort baseUserRepository,
                                   BaseUserQueryPort baseUserQueryPort,
                                   PasswordGeneratorPort passwordGenerator,
                                   UserRepositoryPort userRepository) {
        this(passwordEncoder, baseUserRepository, baseUserQueryPort,
                passwordGenerator, userRepository, UsernamePolicy.defaultUsernamePolicy());
    }

    // Additional constructor for testability (allows injecting custom username policy)
    public UserRegistrationService(PasswordEncoderPort passwordEncoder,
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

    public void register(String username, String name, String surname,
                         String nationalId, String mobileNumber, String email,
                         Role role, AuthenticationProvider authenticationProvider) {
        validateUsernameNotExists(username);
        BaseUser baseUser = createBaseUser(username, role, authenticationProvider);
        User userDetails = createUserDetails(baseUser.getId(), name, surname, nationalId, mobileNumber, email);
        saveUserData(baseUser, userDetails);
    }

    private void validateUsernameNotExists(String username) {
        if (baseUserQueryPort.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
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
}
