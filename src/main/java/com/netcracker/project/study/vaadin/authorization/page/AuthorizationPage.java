package com.netcracker.project.study.vaadin.authorization.page;

import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.admin.components.logo.BottomTeamLogoLink;
import com.netcracker.project.study.vaadin.authorization.components.popups.ClientRegistration;
import com.netcracker.project.study.vaadin.configurations.SecurityConfig;
import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;

@Theme("valo")
@SpringUI(path = "auth")
public class AuthorizationPage extends UI {

    @Autowired
    BottomTeamLogoLink bottomTeamLogo;

    @Autowired
    ClientRegistration clientRegistration;

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private Toastr toastr;

    private Window regAsClientWindow;

    private TextField username;

    private PasswordField password;


    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layoutContent = genLayoutContent();
        setContent(layoutContent);

        Window window = new Window();
        window.setContent(getStartWindow());
        window.center();
        window.setClosable(false);
        window.setResizable(false);
        window.setResponsive(false);
        window.setDraggable(false);
        //window.setModal(true);
        window.setWindowMode(WindowMode.NORMAL);
        //window.setIcon(FontAwesome.REGISTERED);
        UI.getCurrent().addWindow(window);

        initRegWindows();

        layoutContent.addComponent(bottomTeamLogo);
        layoutContent.setComponentAlignment(bottomTeamLogo, Alignment.BOTTOM_CENTER);
    }

    private void initRegWindows() {
        regAsClientWindow = new Window("Client registration");
        regAsClientWindow.center();
        regAsClientWindow.setModal(true);
        regAsClientWindow.setContent(clientRegistration);
    }

    private VerticalLayout genLayoutContent() {
        VerticalLayout layoutContent = new VerticalLayout();
        layoutContent.setMargin(true);
        layoutContent.setSpacing(true);
        layoutContent.setSizeFull();
        return layoutContent;
    }


    private VerticalLayout getStartWindow() {
        VerticalLayout form = new VerticalLayout();
        form.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        form.setMargin(true);
        form.setSpacing(true);
        form.setWidth(520, Unit.PIXELS);

        form.addComponent(genHeader());
        //Label wrongAuth = new Label("<h6>Wrong login or password</h6>", ContentMode.HTML);
        //form.addComponent(wrongAuth);
        form.addComponent(genRegFields());
        form.addComponent(genBottom());

        toastr = new Toastr();
        form.addComponent(toastr);

        return form;
    }

    private HorizontalLayout genHeader() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setWidth(100, Unit.PERCENTAGE);

        Label welcome = new Label("<h4>WELCOME</h4>", ContentMode.HTML);
        welcome.setStyleName(MaterialTheme.LABEL_LIGHT);
        horizontalLayout.addComponent(welcome);
        horizontalLayout.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);
        horizontalLayout.setExpandRatio(welcome, 0.7f);

        Label yanber = new Label("<h3>YanberTaxi</h3>", ContentMode.HTML);
        yanber.setStyleName(MaterialTheme.LABEL_LIGHT);
        horizontalLayout.addComponent(yanber);
        horizontalLayout.setComponentAlignment(yanber, Alignment.MIDDLE_RIGHT);


        horizontalLayout.addComponent(yanber);
        return horizontalLayout;
    }


    private HorizontalLayout genRegFields() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setWidth(100, Unit.PERCENTAGE);

        username = new TextField("Username");
        username.setIcon(VaadinIcons.USER);
        horizontalLayout.addComponent(username);
        horizontalLayout.setComponentAlignment(username, Alignment.MIDDLE_LEFT);
        horizontalLayout.setExpandRatio(username, 0.40f);

        password = new PasswordField("Password");
        password.setIcon(VaadinIcons.LOCK);
        horizontalLayout.addComponent(password);
        horizontalLayout.setExpandRatio(password, 0.40f);

        Button signIn = new Button("Sign In");
        signIn.setStyleName(MaterialTheme.BUTTON_PRIMARY);
        horizontalLayout.addComponent(signIn);
        horizontalLayout.setComponentAlignment(signIn, Alignment.BOTTOM_CENTER);
        horizontalLayout.setExpandRatio(signIn, 0.20f);
        signIn.addClickListener(clickEvent -> {
            Authentication auth = new UsernamePasswordAuthenticationToken(username.getValue(), password.getValue());
            try {
                Authentication authenticated = daoAuthenticationProvider.authenticate(auth);
                SecurityContextHolder.getContext().setAuthentication(authenticated);
                if (userDetailsService.hasRole(Role.ROLE_CLIENT.name())) {
                    getPage().setLocation("/client");
                } else if (userDetailsService.hasRole(Role.ROLE_DRIVER.name())){
                    getPage().setLocation("/driver");
                } else if (userDetailsService.hasRole(Role.ROLE_ADMIN.name())) {
                    getPage().setLocation("/admin");
                }
            } catch (BadCredentialsException e){
                toastr.toast(ToastBuilder.error("Wrong login or password!").build());
            }
        });

        return horizontalLayout;
    }

    private HorizontalLayout genBottom() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setWidth(100, Unit.PERCENTAGE);

        CheckBox rememberMe = new CheckBox("Remember me");
        horizontalLayout.addComponent(rememberMe);
        horizontalLayout.setComponentAlignment(rememberMe, Alignment.BOTTOM_LEFT);
        horizontalLayout.setExpandRatio(rememberMe, 0.6f);

        PopupView popupView = new PopupView("Registration", genAsWho());
        horizontalLayout.addComponent(popupView);

        horizontalLayout.setComponentAlignment(popupView, Alignment.BOTTOM_LEFT);
        horizontalLayout.setExpandRatio(popupView, 0.4f);

        return horizontalLayout;
    }

    private VerticalLayout genAsWho() {
        VerticalLayout verticalLayout = new VerticalLayout();

        Button asDriver = new Button("As driver");
        asDriver.setIcon(VaadinIcons.CAR);
        asDriver.setStyleName(MaterialTheme.BUTTON_LINK);
        verticalLayout.addComponent(asDriver);
        asDriver.addClickListener(clock -> {

        });

        Button asClient = new Button("As Client");
        asClient.setIcon(VaadinIcons.USER);
        asClient.setStyleName(MaterialTheme.BUTTON_LINK);
        verticalLayout.addComponent(asClient);
        asDriver.addClickListener(clock -> {

        });

        return verticalLayout;
    }

}
