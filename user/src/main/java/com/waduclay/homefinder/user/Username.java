package com.waduclay.homefinder.user;


import static com.waduclay.homefinder.shared.InputGuard.againstEmptiness;


import java.util.Set;
import java.util.regex.Pattern;

/**
 * Represents a validated username following business rules
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
class Username {
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 20;
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_.-]+$");
    private static final Set<String> RESERVED_NAMES = Set.of(
            "admin", "root", "system", "support", "help", "info"
    );

    private final String value;

    private Username(final String value) {
        this.value = value;
    }

    /**
     * Creates a Username instance after validating against business rules
     * @param value The raw username input
     * @return Validated Username object
     * @throws IllegalArgumentException if validation fails
     */
    public static Username from(final String value) {
        againstEmptiness(value, "username");
        validateLength(value);
        validateCharacters(value);
        validateNotReserved(value);
        validateNotOffensive(value);

        return new Username(value.toLowerCase()); // Store in lowercase for case-insensitive handling
    }

    private static void validateLength(String value) {
        if (value.length() < MIN_LENGTH || value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Username must be between %d and %d characters", MIN_LENGTH, MAX_LENGTH)
            );
        }
    }

    private static void validateCharacters(String value) {
        if (!USERNAME_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Username can only contain letters, numbers, underscores, periods, and hyphens"
            );
        }
    }

    private static void validateNotReserved(String value) {
        if (RESERVED_NAMES.contains(value.toLowerCase())) {
            throw new IllegalArgumentException("This username is reserved and cannot be used");
        }
    }

    private static void validateNotOffensive(String value) {
        // In a real implementation, this would check against a database or list of offensive terms
        // This is just a placeholder implementation

        if (value.toLowerCase().contains("fuck") || value.toLowerCase().contains("shit")) {
            throw new IllegalArgumentException("Username contains offensive language");
        }
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username = (Username) o;
        return value.equalsIgnoreCase(username.value); // Case-insensitive comparison
    }

    @Override
    public int hashCode() {
        return value.toLowerCase().hashCode();
    }
}
