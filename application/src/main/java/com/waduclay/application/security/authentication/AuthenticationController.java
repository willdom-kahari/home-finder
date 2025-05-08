package com.waduclay.application.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthenticationController {
    private static final String authorizationRequestBaseUri = "oauth2/authorization";
    private final AuthenticationService authenticationService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    @GetMapping
    public String loginPage(Model model) {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));

        AuthenticationRequestDTO authRequest = new AuthenticationRequestDTO();
        model.addAttribute("urls", oauth2AuthenticationUrls);
        model.addAttribute("auth", authRequest);
        return "login";
    }

    @PostMapping
    public String signIn(@Valid @ModelAttribute("auth") AuthenticationRequestDTO authenticationRequest,
                         HttpServletRequest request,
                         HttpServletResponse response
    ) {
        return authenticationService.authenticate(authenticationRequest, request, response);
    }

}
