package com.waduclay.homefinder.shared;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * Encapsulates username validation rules
 *
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public interface UsernamePolicy {
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

    static UsernamePolicy defaultUsernamePolicy() {
        return new DefaultUsernamePolicy();
    }

    static ConfigurableUsernamePolicy.Builder configurableUsernamePolicy() {
        return ConfigurableUsernamePolicy.builder();
    }

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

    final class ConfigurableUsernamePolicy implements UsernamePolicy {
        private final int minLength;
        private final int maxLength;
        private final Pattern allowedChars;
        private final Set<String> reservedNames;
        private final Set<String> offensiveTerms;

        private ConfigurableUsernamePolicy(Builder builder) {
            this.minLength = builder.minLength;
            this.maxLength = builder.maxLength;
            this.allowedChars = builder.allowedChars;
            this.reservedNames = Set.copyOf(builder.reservedNames);
            this.offensiveTerms = Set.copyOf(builder.offensiveTerms);
        }

        public static Builder builder() {
            return new Builder();
        }

        @Override
        public void validate(String username) {
            UsernamePolicy.validateLength(username, minLength, maxLength);
            UsernamePolicy.validateCharacters(username, allowedChars);
            UsernamePolicy.validateNotReserved(username, reservedNames);
            UsernamePolicy.validateNotOffensive(username, offensiveTerms);
        }

        public static final class Builder {
            private int minLength = 4;
            private int maxLength = 20;
            private Pattern allowedChars = Pattern.compile("^[a-zA-Z0-9_.-]+$");
            private Set<String> reservedNames = Set.of();
            private Set<String> offensiveTerms = Set.of();

            public Builder withLengthRange(int minLength, int maxLength) {
                this.minLength = minLength;
                this.maxLength = maxLength;
                return this;
            }

            public Builder withAllowedCharacters(Pattern pattern) {
                this.allowedChars = pattern;
                return this;
            }

            public Builder withAllowedCharacters(String regex) {
                return withAllowedCharacters(Pattern.compile(regex));
            }

            public Builder withReservedNames(Set<String> reservedNames) {
                this.reservedNames = reservedNames;
                return this;
            }

            public Builder withReservedNames(String... reservedNames) {
                return withReservedNames(Set.of(reservedNames));
            }

            public Builder withOffensiveTerms(Set<String> offensiveTerms) {
                this.offensiveTerms = offensiveTerms;
                return this;
            }

            public Builder withOffensiveTerms(String... offensiveTerms) {
                return withOffensiveTerms(Set.of(offensiveTerms));
            }

            public UsernamePolicy build() {
                return new ConfigurableUsernamePolicy(this);
            }
        }
    }
}
