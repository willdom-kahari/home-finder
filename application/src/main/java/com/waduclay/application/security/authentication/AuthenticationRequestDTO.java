package com.waduclay.application.security.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthenticationRequestDTO {
    @NotBlank
    @NotEmpty
    private String username;
    private String password;
}
