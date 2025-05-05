package com.waduclay.homefinder.shared.auth;

import com.waduclay.homefinder.ports.PasswordEncoder;
import com.waduclay.homefinder.shared.InputGuard;

import java.util.regex.Pattern;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public final class Password {
    private final String value;

    private Password(String value) {
        this.value = value;
    }

    public static Password of(String value, PasswordEncoder encoder) {
        return of(value, PasswordValidator.defaultValidator(), encoder);
    }

    public static Password of(String value, PasswordValidator validator, PasswordEncoder encoder) {
        InputGuard.againstEmptiness(value, "password");
        validator.validate(value);
        String encryptedPassword = encoder.encode(value);
        return new Password(encryptedPassword);
    }

    public static Password from(String password) {
        return new Password(password);
    }

    public String getValue() {
        return this.value;
    }

    public static class PasswordValidator {
        private final int minLength;
        private final int maxLength;
        private final boolean requireUppercase;
        private final boolean requireLowercase;
        private final boolean requireDigit;
        private final boolean requireSpecialChar;
        private Pattern pattern;

        private PasswordValidator(Builder builder) {
            this.minLength = builder.minLength;
            this.maxLength = builder.maxLength;
            this.requireUppercase = builder.requireUppercase;
            this.requireLowercase = builder.requireLowercase;
            this.requireDigit = builder.requireDigit;
            this.requireSpecialChar = builder.requireSpecialChar;
            buildPattern();
        }

        public static PasswordValidator defaultValidator() {
            return new Builder()
                    .minLength(8)
                    .maxLength(30)
                    .requireUppercase()
                    .requireLowercase()
                    .requireDigit()
                    .requireSpecialChar()
                    .build();
        }

        public static Builder builder() {
            return new Builder();
        }

        private void buildPattern() {
            StringBuilder patternBuilder = new StringBuilder("^");

            if (requireUppercase) {
                patternBuilder.append("(?=.*[A-Z])");
            }
            if (requireLowercase) {
                patternBuilder.append("(?=.*[a-z])");
            }
            if (requireDigit) {
                patternBuilder.append("(?=.*\\d)");
            }
            if (requireSpecialChar) {
                patternBuilder.append("(?=.*[!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?])");
            }

            patternBuilder.append(".{").append(minLength).append(",").append(maxLength).append("}$");
            this.pattern = Pattern.compile(patternBuilder.toString());
        }

        public void validate(String value) {
            if (value == null) {
                throw new IllegalArgumentException("Password cannot be null");
            }


            if (!pattern.matcher(value).matches()) {
                StringBuilder error = new StringBuilder("Password must be ")
                        .append(minLength).append("-").append(maxLength).append(" characters long with at least:");

                if (requireUppercase) error.append(" 1 uppercase,");
                if (requireLowercase) error.append(" 1 lowercase,");
                if (requireDigit) error.append(" 1 digit,");
                if (requireSpecialChar) error.append(" 1 special character,");

                // Remove trailing comma
                error.setLength(error.length() - 1);
                throw new IllegalArgumentException(error.toString());
            }
        }

        public static class Builder {
            private int minLength = 1;
            private int maxLength = 255;
            private boolean requireUppercase = false;
            private boolean requireLowercase = false;
            private boolean requireDigit = false;
            private boolean requireSpecialChar = false;

            public Builder minLength(int minLength) {
                this.minLength = minLength;
                return this;
            }

            public Builder maxLength(int maxLength) {
                this.maxLength = maxLength;
                return this;
            }

            public Builder requireUppercase() {
                this.requireUppercase = true;
                return this;
            }

            public Builder requireLowercase() {
                this.requireLowercase = true;
                return this;
            }

            public Builder requireDigit() {
                this.requireDigit = true;
                return this;
            }

            public Builder requireSpecialChar() {
                this.requireSpecialChar = true;
                return this;
            }


            public PasswordValidator build() {
                if (minLength <= 0) {
                    throw new IllegalArgumentException("Minimum length must be positive");
                }
                if (maxLength < minLength) {
                    throw new IllegalArgumentException("Maximum length must be greater than minimum length");
                }
                return new PasswordValidator(this);
            }
        }
    }
}
