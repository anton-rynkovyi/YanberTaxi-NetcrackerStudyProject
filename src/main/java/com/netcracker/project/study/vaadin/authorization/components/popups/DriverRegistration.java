package com.netcracker.project.study.vaadin.authorization.components.popups;

import com.netcracker.project.study.vaadin.common.components.PhoneField;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.context.annotation.Scope;
import org.vaadin.addons.Toastr;

@SpringComponent
@Scope(value = "prototype")
public class DriverRegistration extends Window {

    private Toastr toastr;

    public DriverRegistration() {
        VerticalLayout root = genRootLayout();
        root.addComponent(genFields());
        toastr = new Toastr();
        root.addComponent(toastr);
        center();
        setIcon(VaadinIcons.TAXI);
        setModal(true);
        setResizable(false);
        setCaption(" Driver registration");
        setContent(root);
    }

    private VerticalLayout genRootLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth(800, Unit.PIXELS);
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        return verticalLayout;
    }

    private Component genFields() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        VerticalLayout verticalLayout1 = new VerticalLayout();
        PhoneField phone = new PhoneField("Phone number");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");

        Button cancel = new Button("Cancel", VaadinIcons.EXIT);
        cancel.setWidth(100, Unit.PIXELS);
        verticalLayout1.addComponents(phone, password1, password2, cancel);

        VerticalLayout verticalLayout2 = new VerticalLayout();
        TextField lastName = new TextField("Last name");
        TextField firstName = new TextField("Last name");
        TextField middleName = new TextField("Middle name");

        Button ok = new Button("Ok", VaadinIcons.USER_CHECK);
        ok.setWidth(100, Unit.PIXELS);
        verticalLayout2.addComponents(lastName, firstName, middleName, ok);
        verticalLayout2.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT);

        return horizontalLayout;
    }

}
