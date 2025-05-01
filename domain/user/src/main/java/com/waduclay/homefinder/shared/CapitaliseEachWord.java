package com.waduclay.homefinder.shared;


/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public final class CapitaliseEachWord {
    private String value;

    private CapitaliseEachWord(String value) {
        InputGuard.againstEmptiness(value, "value");
        this.value = value;
    }

    public static String from(String value) {
        CapitaliseEachWord normaliser = new CapitaliseEachWord(value.toLowerCase());
        normaliser.capitalise();
        return normaliser.value;
    }

    private void capitalise() {
        char[] chars = value.toCharArray();
        boolean capitaliseNext = true; // Flag to indicate if the next character should be capitalised

        for (int i = 0; i < chars.length; i++) {
            if (Character.isWhitespace(chars[i]) || chars[i] == ',') {
                capitaliseNext = true; // Set flag to capitalise the next character
            } else if (capitaliseNext && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]); // Capitalise the current character
                capitaliseNext = false; // Reset the flag
            }
        }

        this.value = new String(chars);
    }
}
