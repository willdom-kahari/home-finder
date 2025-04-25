package com.waduclay.homefinder.user;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Encapsulates username validation rules
 *
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public interface UsernamePolicy {
    void validate(String username);

    /**
     * Default implementation with standard rules
     */
    class DefaultUsernamePolicy implements UsernamePolicy {
        private static final int MIN_LENGTH = 4;
        private static final int MAX_LENGTH = 20;
        private static final Pattern ALLOWED_CHARS = Pattern.compile("^[a-zA-Z0-9_.-]+$");
        private static final Set<String> RESERVED_NAMES = Set.of(
                "admin", "root", "system", "support", "help", "info"
        );

        @Override
        public void validate(String username) {
            validateLength(username);
            validateCharacters(username);
            validateNotReserved(username);
            validateNotOffensive(username);
        }

        private void validateLength(String username) {
            if (username.length() < MIN_LENGTH || username.length() > MAX_LENGTH) {
                throw new IllegalArgumentException(
                        String.format("Username must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH)
                );
            }
        }

        private void validateCharacters(String username) {
            if (!ALLOWED_CHARS.matcher(username).matches()) {
                throw new IllegalArgumentException(
                        "Username can only contain letters, numbers, underscores, periods, and hyphens"
                );
            }
        }

        private void validateNotReserved(String username) {
            if (RESERVED_NAMES.contains(username.toLowerCase())) {
                throw new IllegalArgumentException("This username is reserved and cannot be used");
            }
        }

        private void validateNotOffensive(String username) {
            // Could be injected from configuration
            if (username.toLowerCase().contains("fuck") || username.toLowerCase().contains("shit")) {
                throw new IllegalArgumentException("Username contains offensive language");
            }
        }
    }
}
