package com.waduclay.homefinder.shared;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */

public final class Name {
    private final String value;

    public Name(String value) {
        this.value = CapitaliseEachWord.from(value);
    }

    public static Name from(String value) {
        return new Name(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name lastName = (Name) o;
        return value.equals(lastName.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "LastName{" + "value='" + value + '\'' + '}';
    }
}
