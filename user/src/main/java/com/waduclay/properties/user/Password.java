package com.waduclay.properties.user;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
class Password {
    private final String value;

    private Password(String value) {
        this.value = value;
    }

    static Password from(String value){
        return new Password(value);
    }

    String getValue() {
        return value;
    }
}
