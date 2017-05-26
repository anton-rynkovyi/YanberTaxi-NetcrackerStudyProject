package com.netcracker.project.study.vaadin.admin.components.popup;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.status.DriverStatus;
import com.netcracker.project.study.model.driver.status.DriverStatusValues;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversGrid;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversRequestsGrid;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;

@SpringComponent
public class DriverRequestInfoPopUp extends VerticalLayout{

    Driver driver;

    @Autowired
    DriversRequestsGrid driversRequestsGrid;

    @Autowired
    DriversGrid driversGrid;

    @Autowired
    AdminService adminService;


    public void init(Driver driver) {
        this.driver = driver;
        removeAllComponents();
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setWidthUndefined();
        rootLayout.setSpacing(true);
        rootLayout.setMargin(true);
        setTextFields(rootLayout); // вынести из инита, чтобы не создавался драйвер при старте бина.
        addComponent(rootLayout);
    }

    private void setTextFields(VerticalLayout rootLayout) {
        rootLayout.addComponent(setDriverAndCarInfoLayout());
        rootLayout.addComponent(setControlButtonsLayout());
    }

    private HorizontalLayout setDriverAndCarInfoLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout driverForm = new VerticalLayout();

        Label name = new Label( "<h2><b>" + driver.getFirstName() + " " + driver.getLastName()+ "</b></h2><hr>",
                ContentMode.HTML);
        Label midName = new Label("Middle name: <i>" + driver.getMiddleName() + "</i>", ContentMode.HTML);
        Label phone = new Label("Phone: <i>" + driver.getPhoneNumber() + "</i>", ContentMode.HTML);
        Label email = new Label("Email: <i>" + driver.getEmail() + "</i>", ContentMode.HTML);
        Label exp = new Label("Experience: <i>" + driver.getExperience() + " years </i>", ContentMode.HTML);
        Label status = new Label("Status: <i>" + driver.getStatus() + "</i>", ContentMode.HTML);
        driverForm.addComponents(name, midName, phone, email, exp, status);

        Panel driverPanel = new Panel(driverForm);
        driverPanel.setWidth(400, Unit.PIXELS);

        FormLayout carForm = new FormLayout();
        carForm.addComponent(new Label("In development..."));
        Panel carPanel = new Panel(carForm);
        carPanel.setWidth(400, Unit.PIXELS);

        horizontalLayout.addComponent(driverPanel);
        horizontalLayout.addComponent(carPanel);

        return horizontalLayout;
    }

    private HorizontalLayout setControlButtonsLayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthUndefined();
        horizontalLayout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);

        Button btnDecline = new Button("Decline");
        btnDecline.addClickListener(clickEvent -> {
            adminService.deleteModel(driver);
            List<Driver> driverRequestsList = driversRequestsGrid.getDriversRequestsList();
            driverRequestsList.remove(driver);
            driversRequestsGrid.getDriversRequestsGrid().setItems(driverRequestsList);
            driversRequestsGrid.refreshGrid();
            driversRequestsGrid.getDriversRequestSubWindow().close();
        });
        horizontalLayout.addComponent(btnDecline);

        Button btnApprove = new Button("Approve");
        btnApprove.addClickListener(clickEvent -> {
            driver.setStatus(DriverStatusValues.OFF_DUTY);
            adminService.updateModel(driver);
            List<Driver> driversList = driversGrid.getDriversList();
            driversList.add(driver);
            driversGrid.getDriversGrid().setItems(driversList);
            driversGrid.refreshGrid();

            List<Driver> driverRequestsList = driversRequestsGrid.getDriversRequestsList();
            driverRequestsList.remove(driver);
            driversRequestsGrid.getDriversRequestsGrid().setItems(driverRequestsList);
            driversRequestsGrid.refreshGrid();

            driversRequestsGrid.getDriversRequestSubWindow().close();
        });

        horizontalLayout.addComponent(btnApprove);

        return horizontalLayout;
    }
}
