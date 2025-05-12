package com.waduclay.application.security.authentication.ui.view;


import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Route("/home")
public class HomePage extends Main {
    public HomePage() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        setText("Home Page for " + name);
    }
}
