package com.waduclay.application.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping
    public String loginPage(Model model) {
        model.addAttribute("auth", new AuthenticationRequest());
        return "login";
    }

    @PostMapping
    public String signIn(
            @Valid @ModelAttribute("auth") AuthenticationRequest authenticationRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authenticationService.authenticate(authenticationRequest, request, response);
    }

}
