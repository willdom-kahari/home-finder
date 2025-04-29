package com.waduclay.application.config;


import io.github.willdomkahari.generator.CharacterGenerator;
import io.github.willdomkahari.generator.Characters;
import io.github.willdomkahari.generator.GeneratorRule;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
public final class RandomGenerator {
    private static final CharacterGenerator characterGenerator = new CharacterGenerator();

    private RandomGenerator() {
    }

    public static String characters(int length) {
        return characterGenerator.generateCharacters(
                length, new GeneratorRule(Characters.CAPITAL),
                new GeneratorRule(Characters.NUMERIC),
                new GeneratorRule(Characters.SPECIAL),
                new GeneratorRule(Characters.SMALL)
        );
    }
}
