package com.waduclay.properties.user;


import static com.waduclay.properties.shared.InputGuard.againstEmptiness;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
class Username {
    private final String value;
    private Username(final String value) {
        this.value = value;
    }

    static Username from (final String value){
        againstEmptiness(value, "username");
        return new Username(value);
    }

    String getValue() {
        return this.value;
    }
}
