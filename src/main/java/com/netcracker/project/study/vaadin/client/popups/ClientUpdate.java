package com.netcracker.project.study.vaadin.client.popups;

import com.google.common.collect.ImmutableList;
import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.facade.UserFacade;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.client.page.ClientPage;
import com.netcracker.project.study.vaadin.common.components.PhoneField;
import com.netcracker.project.study.vaadin.driver.page.DriverPage;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
@Scope(value = "prototype")
public class ClientUpdate extends Window{

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PersistenceFacade persistenceFacade;

    @Autowired
    UserFacade userFacade;

    @Autowired
    PasswordEncoder passwordEncoder;

    private Toastr toastr;
    private PhoneField phone;
    private PasswordField password1;
    private PasswordField password2;
    private TextField lastName;
    private TextField firstName;
    private TextField middleName;


    public ClientUpdate() {

    }

    public void init(User user,Client client) {
        VerticalLayout root = genRootLayout();
        root.addComponent(genFields());
        toastr = new Toastr();
        root.addComponent(toastr);
        center();
        setIcon(VaadinIcons.USER);
        setModal(true);
        setResizable(false);
        setCaption(" Client card");
        setContent(root);
    }
    private VerticalLayout genRootLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth(600, Unit.PIXELS);
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        return verticalLayout;
    }

    private Component genFields() {
        Client client = userDetailsService.getCurrentUser();
        User user = userDetailsService.getUser();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout verticalLayout2 = new VerticalLayout();
        phone = new PhoneField("Phone number");
        phone.setValue(client.getPhoneNumber());
        password1 = new PasswordField("Password");
        password2 = new PasswordField("Confirm password");
        Button cancel = new Button("Cancel", VaadinIcons.EXIT);
        cancel.setWidth(100, Unit.PIXELS);
        verticalLayout2.addComponents(phone, password1, password2, cancel);

        VerticalLayout verticalLayout1 = new VerticalLayout();
        lastName = new TextField("Last name");
        lastName.setValue(client.getLastName());
        firstName = new TextField("First name");
        firstName.setValue(client.getFirstName());
        middleName = new TextField("Middle name");
        middleName.setValue(client.getMiddleName());
        Button ok = new Button("Ok", VaadinIcons.USER_CHECK);
        ok.setWidth(100, Unit.PIXELS);
        verticalLayout1.addComponents(lastName, firstName, middleName, ok);
        verticalLayout1.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT);

        ok.addClickListener(event -> {
            if (lastName.isEmpty()
                    || firstName.isEmpty()
                    || middleName.isEmpty()
                    || phone.isEmpty()
                    || password1.isEmpty()
                    || password2.isEmpty()) {
                toastr.toast(ToastBuilder.error("All fields must be filled!").build());
                return;
            }
            if (!password1.getValue().equals(password2.getValue())) {
                toastr.toast(ToastBuilder.error("Passwords don't match!").build());
                password1.clear();
                password2.clear();
                return;
            }


            client.setFirstName(firstName.getValue());
            client.setLastName(lastName.getValue());
            client.setMiddleName(middleName.getValue());
            client.setPhoneNumber(phone.getValue());
            client.setPoints(BigInteger.valueOf(0));
            persistenceFacade.update(client);

            user.setObjectId(client.getObjectId());
            user.setUsername(client.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(password2.getValue()));
            user.setEnabled(true);
            user.setAuthorities(ImmutableList.of(Role.ROLE_CLIENT));
            userFacade.updateUser(user);
            toastr.toast(ToastBuilder.success(
                    "You are successfully registered!\nEnter you login and password to continue the work.")
                    .build());
            close();
            phone.clear();
            password1.clear();
            password2.clear();
            lastName.clear();
            firstName.clear();
            middleName.clear();
            String driverName = client.getFirstName() + " " + client.getLastName();
            ((ClientPage) UI.getCurrent()).updateClientName(driverName);
        });
        cancel.addClickListener(event -> {
            close();
            phone.clear();
            password1.clear();
            password2.clear();
            lastName.clear();
            firstName.clear();
            middleName.clear();
        });

        addCloseListener(e -> {
           cancel.click();
        });

        horizontalLayout.addComponents(verticalLayout2, verticalLayout1);

        return horizontalLayout;
    }
}
