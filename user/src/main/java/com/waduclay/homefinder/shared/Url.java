package com.waduclay.homefinder.shared;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public final class Url {
    private final String value;

    private Url(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }
        // Add URL format validation
        this.value = value;
    }

    public static Url from(String value){
        return new Url(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return value.equals(url.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "Url{" + "value='" + value + '\'' + '}';
    }
}
