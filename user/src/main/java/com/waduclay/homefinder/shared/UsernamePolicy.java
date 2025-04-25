package com.waduclay.homefinder.shared;

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
        private static final Set<String> OFFENSIVE_TERMS = Set.of("fuck", "shit");

        @Override
        public void validate(String username) {
            validateLength(username, MIN_LENGTH, MAX_LENGTH);
            validateCharacters(username, ALLOWED_CHARS);
            validateNotReserved(username, RESERVED_NAMES);
            validateNotOffensive(username, OFFENSIVE_TERMS);
        }
    }

    class ConfigurableUsernamePolicy implements UsernamePolicy {
        private final int minLength;
        private final int maxLength;
        private final Pattern allowedChars = Pattern.compile("^[a-zA-Z0-9_.-]+$");
        private final Set<String> reservedNames;
        private final Set<String> offensiveTerms;

        public ConfigurableUsernamePolicy(int minLength, int maxLength,
                                          Set<String> reservedNames,
                                          Set<String> offensiveTerms) {
            this.minLength = minLength;
            this.maxLength = maxLength;
            this.reservedNames = Set.copyOf(reservedNames);
            this.offensiveTerms = Set.copyOf(offensiveTerms);
        }

        @Override
        public void validate(String username) {
            validateLength(username, minLength, maxLength);
            validateCharacters(username, allowedChars);
            validateNotReserved(username, reservedNames);
            validateNotOffensive(username, offensiveTerms);
        }
    }

    // Shared validation methods
    static void validateLength(String username, int minLength, int maxLength) {
        if (username.length() < minLength || username.length() > maxLength) {
            throw new IllegalArgumentException(
                    String.format("Username must be between %d and %d characters", minLength, maxLength)
            );
        }
    }

    static void validateCharacters(String username, Pattern allowedChars) {
        if (!allowedChars.matcher(username).matches()) {
            throw new IllegalArgumentException(
                    "Username can only contain letters, numbers, underscores, periods, and hyphens"
            );
        }
    }

    static void validateNotReserved(String username, Set<String> reservedNames) {
        if (reservedNames.contains(username.toLowerCase())) {
            throw new IllegalArgumentException("This username is reserved and cannot be used");
        }
    }

    static void validateNotOffensive(String username, Set<String> offensiveTerms) {
        String lowerUsername = username.toLowerCase();
        if (offensiveTerms.stream().anyMatch(lowerUsername::contains)) {
            throw new IllegalArgumentException("Username contains offensive language");
        }
    }
}
