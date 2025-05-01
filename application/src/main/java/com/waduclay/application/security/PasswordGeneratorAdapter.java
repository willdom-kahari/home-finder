package com.waduclay.application.security;

import com.waduclay.homefinder.ports.PasswordGenerator;
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
}
