package com.netcracker.project.study.vaadin.admin.components.popup;

import com.netcracker.project.study.vaadin.admin.components.grids.DriversRequestsGrid;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringComponent
public class DriverInfoPopUp extends VerticalLayout{

    @Autowired
    DriversRequestsGrid driversRequestsGrid;

    @PostConstruct
    private void init() {
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setWidth("800");
        rootLayout.setSpacing(true);
        rootLayout.setMargin(true);
        openWindowWithTextFields(rootLayout);
        addComponent(rootLayout);
    }

    private void openWindowWithTextFields(VerticalLayout rootLayout) {
        rootLayout.addComponent(setDriverAndCarInfoLayout());
        rootLayout.addComponent(setControlButtonsLayout());
    }

    private HorizontalLayout setDriverAndCarInfoLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        FormLayout driverForm = new FormLayout();
        driverForm.addComponent(new Label("Id development..."));
        Panel driverPanel = new Panel(driverForm);

        FormLayout carForm = new FormLayout();
        carForm.addComponent(new Label("Id development..."));
        Panel carPanel = new Panel(carForm);

        horizontalLayout.addComponent(driverPanel);
        horizontalLayout.addComponent(carPanel);
        return horizontalLayout;
    }

    private HorizontalLayout setControlButtonsLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button btnDecline = new Button("Decline");
        btnDecline.addClickListener(clickEvent -> {});
        horizontalLayout.addComponent(btnDecline);

        Button btnApprove = new Button("Approve");
        btnDecline.addClickListener(clickEvent -> {});
        horizontalLayout.addComponent(btnApprove);

        return horizontalLayout;
    }
}
