package com.waduclay.application.security;

import com.waduclay.homefinder.ports.PasswordGenerator;
import io.github.willdomkahari.generator.CharacterGenerator;
import io.github.willdomkahari.generator.Characters;
import io.github.willdomkahari.generator.GeneratorRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Component
@RequiredArgsConstructor
public class PasswordGeneratorAdapter implements PasswordGenerator {
    @Override
    public String generate() {
        return RandomGenerator.characters(8);
    }

    static final class RandomGenerator {
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
}


