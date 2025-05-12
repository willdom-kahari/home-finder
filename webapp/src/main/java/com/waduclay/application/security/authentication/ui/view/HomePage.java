package com.waduclay.application.security.authentication.ui.view;


import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

/**
 * @author <a href="mailto:developer.wadu@gmail.com">Willdom Kahari</a>
 */
@Route("/home")
public class HomePage extends Main {
    public HomePage() {
        Object user = VaadinSession.getCurrent().getSession().getAttribute("user");
        System.out.println("user = " + user.toString());
        setTitle("Home Page for " + user);
    }
}
