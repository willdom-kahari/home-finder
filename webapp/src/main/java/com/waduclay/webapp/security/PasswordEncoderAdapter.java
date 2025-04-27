package com.waduclay.webapp.security;


import com.waduclay.homefinder.ports.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements PasswordEncoderPort {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String encrypt(String password) {
        return passwordEncoder.encode(password);
    }
}
