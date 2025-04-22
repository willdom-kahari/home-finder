package com.waduclay.homefinder.user;


import static com.waduclay.homefinder.shared.InputGuard.againstEmptiness;

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
