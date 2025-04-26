package com.waduclay.homefinder.shared;

import java.util.Objects;

/**
 * Represents a validated username following business rules
 */
public final class Username {
    private final String value;

    private Username(String value) {
        this.value = value.toLowerCase(); // Immutable and lowercase
    }

    /**
     * Factory method with injected policy
     */
    public static Username of(String value, UsernamePolicy policy) {
        Objects.requireNonNull(policy, "UsernamePolicy cannot be null");
        InputGuard.againstEmptiness(value, "username");

        policy.validate(value); // All validation delegated to policy
        return new Username(value);
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Username username = (Username) o;
        return value.equalsIgnoreCase(username.value);
    }

    @Override
    public int hashCode() {
        return value.toLowerCase().hashCode();
    }
}
