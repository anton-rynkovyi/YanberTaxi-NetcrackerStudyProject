package com.netcracker.project.study.vaadin.admin.components.popup;

import com.google.common.collect.ImmutableList;
import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.model.admin.Admin;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.facade.UserFacade;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.vaadin.common.components.PhoneField;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;

import java.math.BigInteger;

@SpringComponent
@Scope(value = "prototype")
public class AdminUpdate extends Window{

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
    private TextField email;


    public AdminUpdate() {

    }

    public void init() {
        VerticalLayout root = genRootLayout();
        root.addComponent(genFields());
        toastr = new Toastr();
        root.addComponent(toastr);
        center();
        setIcon(VaadinIcons.USER);
        setModal(true);
        setResizable(false);
        setCaption(" Admin card");
        setContent(root);
    }
    private VerticalLayout genRootLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth(300, Unit.PIXELS);
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        return verticalLayout;
    }

    private Component genFields() {
        Admin admin = userDetailsService.getCurrentUser();
        User user = userDetailsService.getUser();
       // HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout verticalLayout2 = new VerticalLayout();
        email = new TextField("email");
        email.setValue(admin.getEmail());
        phone = new PhoneField("Phone number");
        phone.setValue(admin.getPhoneNumber());
        password1 = new PasswordField("Password");
        password2 = new PasswordField("Confirm password");
        Button cancel = new Button("Cancel", VaadinIcons.EXIT);
        cancel.setWidth(100, Unit.PIXELS);
        Button ok = new Button("Ok", VaadinIcons.USER_CHECK);
        ok.setWidth(100, Unit.PIXELS);
        HorizontalLayout horizontalLayoutButton = new HorizontalLayout();
        horizontalLayoutButton.addComponents(cancel,ok);
        verticalLayout2.addComponents(email,phone, password1, password2, horizontalLayoutButton);

       // VerticalLayout verticalLayout1 = new VerticalLayout();


       // verticalLayout1.addComponents(email);
        //verticalLayout1.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT);

        ok.addClickListener(event -> {
            if (email.isEmpty()
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


            admin.setEmail(email.getValue());
            admin.setPhoneNumber(phone.getValue());
            persistenceFacade.update(admin);

            user.setObjectId(admin.getObjectId());
            user.setUsername(admin.getPhoneNumber());
            user.setPassword(passwordEncoder.encode(password2.getValue()));
            user.setEnabled(true);
            user.setAuthorities(ImmutableList.of(Role.ROLE_ADMIN));
            userFacade.updateUser(user);
            toastr.toast(ToastBuilder.success(
                    "You are successfully registered!\nEnter you login and password to continue the work.")
                    .build());
            close();
            phone.clear();
            password1.clear();
            password2.clear();
            email.clear();
        });
        cancel.addClickListener(event -> {
            close();
            phone.clear();
            password1.clear();
            password2.clear();
            email.clear();
        });

        addCloseListener(e -> {
           cancel.click();
        });

        //horizontalLayout.addComponents(verticalLayout2);

        return verticalLayout2;
    }
}
