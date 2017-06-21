package com.netcracker.project.study.vaadin.authorization.page;

import com.github.appreciated.material.MaterialTheme;
import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.driver.status.DriverStatusAttr;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.facade.UserFacade;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.admin.components.logo.Copyright;
import com.netcracker.project.study.vaadin.authorization.components.popups.ClientRegistration;
import com.netcracker.project.study.vaadin.authorization.components.popups.DriverRegistration;
import com.netcracker.project.study.vaadin.common.components.PhoneField;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;

@Theme("valo")
@SpringUI(path = "authorization")
@Title("YanberTaxi-Authorization")
public class AuthorizationPage extends UI {

    @Autowired
    Copyright bottomTeamLogo;

    @Autowired
    ClientRegistration clientRegistration;

    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PersistenceFacade persistenceFacade;

    @Autowired
    UserFacade userFacade;

    @Autowired
    ClientRegistration regAsClientWindow;

    @Autowired
    DriverRegistration regAsDriverWindow;

    private Toastr toastr;
    private PhoneField username;
    private PasswordField password;
    private CheckBox rememberMe;
    private Window regWindow;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layoutContent = genLayoutContent();
        setContent(layoutContent);

        regWindow = new Window();
        regWindow.setContent(getStartWindow());
        regWindow.center();
        regWindow.setClosable(false);
        regWindow.setResizable(false);
        regWindow.setResponsive(false);
        regWindow.setDraggable(false);
        //window.setModal(true);
        regWindow.setWindowMode(WindowMode.NORMAL);
        UI.getCurrent().addWindow(regWindow);
        layoutContent.addComponent(bottomTeamLogo);
        layoutContent.setComponentAlignment(bottomTeamLogo, Alignment.BOTTOM_CENTER);
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

        username = new PhoneField("Phone number");
        username.setIcon(VaadinIcons.PHONE);


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
                } else if (userDetailsService.hasRole(Role.ROLE_DRIVER.name())) {
                    Driver driver = userDetailsService.findDriverByUserName(username.getValue());
                    if (driver == null) {
                        toastr.toast(ToastBuilder.error("Wrong login or password!").build());
                    }
                    if (driver.getStatus() == DriverStatusList.APPROVAL) {
                        toastr.toast(ToastBuilder.info("You are not recruited yet. Watch your email address.").build());
                        return;
                    }
                    getPage().setLocation("/driver");
                } else if (userDetailsService.hasRole(Role.ROLE_ADMIN.name())) {
                    getPage().setLocation("/admin");
                }
                if (rememberMe.isEnabled()) {

                }
            } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
                toastr.toast(ToastBuilder.error("Wrong login or password!").build());
                username.clear();
                password.clear();
            }
        });

        return horizontalLayout;
    }

    PopupView popupView;

    private HorizontalLayout genBottom() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(false);
        horizontalLayout.setMargin(false);
        horizontalLayout.setWidth(100, Unit.PERCENTAGE);

        rememberMe = new CheckBox("Remember me");
        horizontalLayout.addComponent(rememberMe);
        horizontalLayout.setComponentAlignment(rememberMe, Alignment.BOTTOM_LEFT);
        horizontalLayout.setExpandRatio(rememberMe, 0.6f);

        popupView = new PopupView("Registration", genAsWho());
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
            addWindow(regAsDriverWindow);
            if (popupView != null && popupView.isPopupVisible()) {
                popupView.setPopupVisible(false);
            }
        });

        Button asClient = new Button("As Client");
        asClient.setIcon(VaadinIcons.USER);
        asClient.setStyleName(MaterialTheme.BUTTON_LINK);
        verticalLayout.addComponent(asClient);
        asClient.addClickListener(clock -> {
            addWindow(regAsClientWindow);
            if (popupView != null && popupView.isPopupVisible()) {
                popupView.setPopupVisible(false);
            }
        });

        return verticalLayout;
    }

}
