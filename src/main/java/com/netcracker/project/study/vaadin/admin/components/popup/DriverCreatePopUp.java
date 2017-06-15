package com.netcracker.project.study.vaadin.admin.components.popup;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversGrid;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversRequestsGrid;
import com.netcracker.project.study.vaadin.admin.page.AdminPage;
import com.netcracker.project.study.vaadin.admin.views.DriversView;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

@SpringComponent
public class DriverCreatePopUp extends VerticalLayout {

    @Autowired AdminService adminService;

    @Autowired DriversGrid driversGrid;

    @Autowired DriversRequestsGrid driversRequestsGrid;

    @PostConstruct
    public void init() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("600");
        layout.setSpacing(true);
        layout.setMargin(true);
        setTextFields(layout);
        addComponent(layout);
    }

    public void setTextFields(VerticalLayout layout) {

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

        TextField status = new TextField("status");
        experience.setIcon(FontAwesome.LONG_ARROW_UP);
        form.addComponent(status);


        layout.addComponent(form);


        Button btnAddToDB = new Button("Add", FontAwesome.PLUS);
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
                driver.setHireDate(new Date(System.currentTimeMillis()));
                driver.setExperience(BigInteger.valueOf(Long.parseLong(experience.getValue())));
                driver.setRating(new BigDecimal(4));
                driver.setStatus(new BigInteger(status.getValue()));


                adminService.createModel(driver);
/*
                driversGrid.getApprovedDriversList().add(driver);
                driversGrid.getDriversGrid().setItems(driversGrid.getApprovedDriversList());*/

                driversGrid.refreshGrid();
                driversRequestsGrid.refreshGrid();

                lastName.setValue("");
                firstName.setValue("");
                middleName.setValue("");
                phoneNumber.setValue("");
                email.setValue("");
                experience.setValue("");
                status.setValue("");

                driversGrid.getDriversCreateSubWindow().close();

                //AdminPage.navigator.navigateTo(DriversView.VIEW_NAME);
            }
        });

        layout.addComponent(btnAddToDB);
    }

}
