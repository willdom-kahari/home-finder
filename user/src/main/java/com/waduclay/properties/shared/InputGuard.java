package com.waduclay.properties.shared;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class InputGuard {
    private InputGuard() {
    }

    public static void againstEmptiness(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("%s must not be null or empty".formatted(name));
        }
    }

}
