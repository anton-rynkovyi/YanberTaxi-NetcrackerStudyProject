package com.netcracker.project.study.vaadin.authorization.components.popups;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.persistence.facade.UserFacade;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.netcracker.project.study.services.tools.EmailValidator;
import com.netcracker.project.study.vaadin.common.components.PhoneField;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;

import java.math.BigDecimal;
import java.math.BigInteger;

@SpringComponent
@Scope(value = "prototype")
public class DriverRegistration extends Window {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    CarRegistration carRegistration;

    @Autowired
    PersistenceFacade persistenceFacade;

    @Autowired
    UserFacade userFacade;

    @Autowired
    EmailValidator emailValidator;

    private Toastr toastr;
    private PhoneField phone;
    private PasswordField password1;
    private PasswordField password2;
    private TextField email;
    private TextField lastName;
    private TextField firstName;
    private TextField middleName;
    private TextField exp;

    public DriverRegistration() {
       init();
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
        setCaption(" Driver registration");
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
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        VerticalLayout verticalLayout2 = new VerticalLayout();
        phone = new PhoneField("Phone number");
        phone.setPlaceholder("(068)067-68-53");
        password1 = new PasswordField("Password");
        password2 = new PasswordField("Confirm password");
        email = new TextField("Email");
        Button cancel = new Button("Cancel", VaadinIcons.EXIT);
        cancel.setWidth(100, Unit.PIXELS);
        verticalLayout2.addComponents(phone, password1, password2, email, cancel);

        VerticalLayout verticalLayout1 = new VerticalLayout();
        lastName = new TextField("Last name");
        firstName = new TextField("First name");
        middleName = new TextField("Middle name");
        exp = new TextField("Experience");
        Button next = new Button("Next", VaadinIcons.ARROW_CIRCLE_RIGHT);
        next.setWidth(100, Unit.PIXELS);
        verticalLayout1.addComponents(lastName, firstName, middleName, exp, next);
        verticalLayout1.setComponentAlignment(next, Alignment.MIDDLE_RIGHT);

        next.addClickListener(event -> {
            if (lastName.isEmpty()
                    || firstName.isEmpty()
                    || middleName.isEmpty()
                    || email.isEmpty()
                    || phone.isEmpty()
                    || password1.isEmpty()
                    || password2.isEmpty()
                    || exp.isEmpty()) {
                toastr.toast(ToastBuilder.error("All fields must be filled!").build());
                return;
            }
            if (!password1.getValue().equals(password2.getValue())) {
                toastr.toast(ToastBuilder.error("Passwords don't match!").build());
                password1.clear();
                password2.clear();
                return;
            }
            if (userDetailsService.isLoginExist(phone.getValue())) {
                toastr.toast(ToastBuilder.error("This phone number already exists!").build());
                return;
            }
            if (userDetailsService.isEmailExist(email.getValue())) {
                toastr.toast(ToastBuilder.error("Email address already exists!").build());
                return;
            }
            if (!emailValidator.validate(email.getValue())) {
                toastr.toast(ToastBuilder.error("Email address is not valid!").build());
                return;
            }
            if (password2.getValue().length() < 8) {
                toastr.toast(ToastBuilder.error("Password must be more than " +
                        "" + password2.getValue().length() + " " +
                        " characters").build());
                return;
            }
            for (int i = 0; i < exp.getValue().length(); i++) {
                if (!Character.isDigit(exp.getValue().charAt(i))) {
                    toastr.toast(ToastBuilder.error("Wrong experience format!").build());
                    return;
                }
            }

            Driver driver = new Driver();
            driver.setFirstName(firstName.getValue());
            driver.setLastName(lastName.getValue());
            driver.setMiddleName(middleName.getValue());
            driver.setEmail(email.getValue());
            driver.setPhoneNumber(phone.getValue());
            driver.setExperience(new BigInteger(exp.getValue()));
            driver.setRating(BigDecimal.valueOf(4));
            driver.setStatus(DriverStatusList.APPROVAL);
            driver.setUnbanDate(null);
            carRegistration.setDriverAndPassword(driver, password2.getValue());
            persistenceFacade.create(driver);
            //userFacade.createUser(user);
            carRegistration.initDriverRegWindow(this);
            setContent(carRegistration.getContent());
        });

        cancel.addClickListener(event -> {
            phone.clear();
            password1.clear();
            password2.clear();
            email.clear();
            lastName.clear();
            firstName.clear();
            middleName.clear();
            exp.clear();
            close();
        });

        addCloseListener(e -> {
            cancel.click();
        });

        horizontalLayout.addComponents(verticalLayout2, verticalLayout1);
        return horizontalLayout;
    }
}