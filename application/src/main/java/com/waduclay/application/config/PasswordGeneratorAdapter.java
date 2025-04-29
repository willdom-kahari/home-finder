package com.waduclay.application.config;


import com.waduclay.homefinder.ports.PasswordGeneratorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Component
@RequiredArgsConstructor
public class PasswordGeneratorAdapter implements PasswordGeneratorPort {
    @Override
    public String generate() {
        return RandomGenerator.characters(8);
    }
}
