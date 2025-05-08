package com.waduclay.application.security.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthenticationRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 10, message = "Username must be between 4 and 10 characters")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
}
