package com.netcracker.project.study.vaadin.test.ui;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@SpringComponent
public class DriversCreatePopUp extends CustomComponent {

    @Autowired
    PersistenceFacade facade;

    public DriversCreatePopUp() {
        VerticalLayout layout = new VerticalLayout();
        layout.setHeightUndefined();
        layout.setWidth("360");
        layout.setSpacing(false);
        layout.setMargin(false);
        openWindowWithTextFields(layout);
        setCompositionRoot(layout);
    }

    public void openWindowWithTextFields(VerticalLayout layout) {

        FormLayout form = new FormLayout();

        TextField lastName = new TextField("Last Name");
        lastName.setIcon(FontAwesome.USER);
        lastName.beforeClientResponse(false);
        form.addComponent(lastName);

        TextField firstName = new TextField("First Name");
        firstName.setIcon(FontAwesome.USER);
        lastName.beforeClientResponse(true);
        form.addComponent(firstName);

        TextField middleName = new TextField("Middle Name");
        middleName.setIcon(FontAwesome.USER);
        form.addComponent(middleName);

        TextField phoneNumber = new TextField("Phone number");
        phoneNumber.setIcon(FontAwesome.MOBILE_PHONE);
        form.addComponent(phoneNumber);

        TextField email = new TextField("Email address");
        email.setIcon(FontAwesome.ENVELOPE);
        form.addComponent(email);

        TextField experience = new TextField("Experience");
        experience.setIcon(FontAwesome.LONG_ARROW_UP);
        form.addComponent(experience);

        layout.addComponent(form);

        Button btnAddToDB = new Button(FontAwesome.PLUS);
        btnAddToDB.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Driver driver = new Driver();
                driver.setName(lastName.getValue() +" " + firstName.getValue());
                driver.setLastName(lastName.getValue());
                driver.setFirstName(firstName.getValue());
                driver.setMiddleName(middleName.getValue());
                driver.setPhoneNumber(phoneNumber.getValue());
                driver.setEmail(email.getValue());
                driver.setHireDate(new Timestamp(new Date().getTime()));
                driver.setExperience(BigInteger.valueOf(Long.parseLong(experience.getValue())));
                driver.setStatus(Driver.OFF_DUTY);

                facade.create(driver);
            }
        });
        layout.addComponent(btnAddToDB);
    }
}
