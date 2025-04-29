package com.waduclay.homefinder.shared;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public final class InputGuard {

    private InputGuard() {
    }

    public static void againstEmptiness(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("%s must not be null or empty".formatted(name));
        }
    }

    public static <T> void againstNull(T object, String name) {
        if (object == null) {
            throw new IllegalArgumentException("%s must not be null".formatted(name));
        }
    }

    public static <T> T requireNonNull(T object, String message) {
        againstNull(object, message);
        return object;
    }
}
