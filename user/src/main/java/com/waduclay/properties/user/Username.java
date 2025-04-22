package com.waduclay.properties.user;


import static com.waduclay.properties.user.InputGuard.againstEmptiness;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class Username {
    private final String value;
    private Username(final String value) {
        this.value = value;
    }

    public static Username from (final String value){
        againstEmptiness(value, "username");
        return new Username(value);
    }

    public String getValue() {
        return this.value;
    }
}
