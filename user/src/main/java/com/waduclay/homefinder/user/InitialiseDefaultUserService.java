package com.waduclay.homefinder.user;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class InitialiseDefaultUserService {
    private final PasswordEncoderPort passwordEncoder;
    private final BaseUserRepositoryPort repository;

    public InitialiseDefaultUserService(PasswordEncoderPort passwordEncoder, BaseUserRepositoryPort repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    public void execute(String username, String password){
        Username defaultUsername = Username.from(username);
        Password defaultUserPassword = Password.from(password);
        if (!repository.existsByRole(Role.DEFAULT)){
            String encryptedPassword = passwordEncoder.encrypt(defaultUserPassword.getValue());
            UserAccountStatus status = new UserAccountStatus(false, false, true);
            BaseUser user = BaseUser.of(defaultUsername, Role.DEFAULT, encryptedPassword, status);
            repository.save(user);
        }

    }
}
