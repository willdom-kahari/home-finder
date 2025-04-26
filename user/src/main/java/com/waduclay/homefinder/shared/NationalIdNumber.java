package com.waduclay.homefinder.shared;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A domain value object representing a National Identification Number.
 * Immutable and validated upon creation.
 *
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public final class NationalIdNumber {
    private static final String NATIONAL_ID_REGEX = "^\\d{8}\\d?[a-zA-Z]\\d{2}$";
    private final String value;

    private NationalIdNumber(String normalizedNationalId) {
        this.value = normalizedNationalId;
    }

    /**
     * Factory method for creating a validated NationalIdNumber
     *
     * @param nationalId the raw national ID string
     * @return validated NationalIdNumber instance
     * @throws IllegalArgumentException if the input is invalid
     */
    public static NationalIdNumber from(String nationalId) {
        InputGuard.againstEmptiness(nationalId, "national id");
        String normalized = normalize(nationalId);
        validate(normalized);
        return new NationalIdNumber(normalized);
    }

    private static String normalize(String nationalId) {
        return nationalId.trim()
                .replace(" ", "")
                .replace("-", "")
                .toUpperCase();
    }

    private static void validate(String nationalId) {
        Pattern pattern = Pattern.compile(NATIONAL_ID_REGEX);
        Matcher matcher = pattern.matcher(nationalId);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Invalid National ID format. Please enter a valid ID.");
        }
    }

    public static boolean isValid(String nationalId) {
        try {
            from(nationalId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NationalIdNumber that = (NationalIdNumber) o;
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
