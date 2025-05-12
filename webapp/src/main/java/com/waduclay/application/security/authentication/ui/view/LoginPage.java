package com.waduclay.application.security.authentication.ui.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.waduclay.application.security.authentication.AuthenticationRequest;
import com.waduclay.application.security.authentication.AuthenticationService;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Route("login")
@PageTitle("Login")
public class LoginPage extends Main implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();

    public LoginPage(AuthenticationService authenticationService) {
        addClassName("login-view");
        setSizeFull();

        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("login-layout");
        layout.setSizeFull();
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.setAlignItems(Alignment.CENTER);

        H1 header = new H1("Home Finder");
        header.addClassName("login-header");

        loginForm.setForgotPasswordButtonVisible(false);

        loginForm.addLoginListener(e -> {
            AuthenticationRequest authenticationRequest = new AuthenticationRequest();
            authenticationRequest.setUsername(e.getUsername());
            authenticationRequest.setPassword(e.getPassword());
            boolean isAuthenticated = authenticationService.authenticate(
                  authenticationRequest
            );

            if (isAuthenticated) {
                VaadinSession.getCurrent().setAttribute("user", e.getUsername());

                getUI().ifPresent(ui -> ui.navigate("home"));
            } else {
                loginForm.setError(true);
            }
        });

        layout.add(header, loginForm);
        add(layout);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
        }
    }
}
