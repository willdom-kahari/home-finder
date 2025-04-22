package com.waduclay.properties.user;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public class Password {
    private final String value;

    private Password(String value) {
        this.value = value;
    }

    public static Password from(String value){
        return new Password(value);
    }

    public String getValue() {
        return value;
    }
}
