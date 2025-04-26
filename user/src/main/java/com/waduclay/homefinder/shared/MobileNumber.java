package com.waduclay.homefinder.shared;

import java.util.Objects;

/**
 * A domain value object representing a validated and normalised mobile phone number.
 * Immutable and validated upon creation.
 *
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public final class MobileNumber {
    private static final int INTERNATIONAL_FORMAT_LENGTH = 12; // 263...
    private static final int LOCAL_FORMAT_LENGTH = 10; // 07...
    private static final int SHORT_FORMAT_LENGTH = 9; // 7...
    private static final String COUNTRY_CODE = "263";

    private final String value;

    private MobileNumber(String normalizedNumber) {
        this.value = normalizedNumber;
    }

    /**
     * Factory method for creating a validated MobileNumber
     *
     * @param rawNumber the raw mobile number string
     * @return validated MobileNumber instance
     * @throws IllegalArgumentException if the input is invalid
     */
    public static MobileNumber from(String rawNumber) {
        InputGuard.againstEmptiness(rawNumber, "mobile number");
        String normalized = normalize(rawNumber);
        validate(normalized);
        return new MobileNumber(normalized);
    }

    private static String normalize(String rawNumber) {
        String cleaned = rawNumber.trim().replace(" ", "");

        if (cleaned.startsWith("+2637") && cleaned.length() == 13) {
            return cleaned.substring(1); // Remove +
        }
        if (cleaned.startsWith("07") && cleaned.length() == LOCAL_FORMAT_LENGTH) {
            return cleaned.replaceFirst("0", COUNTRY_CODE);
        }
        if (cleaned.startsWith("7") && cleaned.length() == SHORT_FORMAT_LENGTH) {
            return COUNTRY_CODE + cleaned;
        }
        if (cleaned.startsWith(COUNTRY_CODE) && cleaned.length() == INTERNATIONAL_FORMAT_LENGTH) {
            return cleaned;
        }

        throw new IllegalArgumentException("Invalid mobile number. Ensure correct format.");
    }

    private static void validate(String normalizedNumber) {
        if (!normalizedNumber.startsWith(COUNTRY_CODE)) {
            throw new IllegalArgumentException("Mobile number must start with country code " + COUNTRY_CODE);
        }
        if (normalizedNumber.length() != INTERNATIONAL_FORMAT_LENGTH) {
            throw new IllegalArgumentException("Mobile number must be " + INTERNATIONAL_FORMAT_LENGTH + " digits");
        }
        if (!normalizedNumber.matches("^\\d+$")) {
            throw new IllegalArgumentException("Mobile number must contain only digits");
        }
    }

    public String getValue() {
        return value;
    }

    public String toInternationalFormat() {
        return "+" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MobileNumber that = (MobileNumber) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
