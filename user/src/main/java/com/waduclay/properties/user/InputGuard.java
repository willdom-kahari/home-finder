package com.waduclay.properties.user;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
class InputGuard {
    private InputGuard() {
    }

    static void againstEmptiness(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("%s must not be null or empty".formatted(name));
        }
    }

}
