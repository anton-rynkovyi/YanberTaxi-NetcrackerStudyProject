package com.netcracker.project.study.vaadin.admin.components.popup;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.driver.DriverStatusValues;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversGrid;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversRequestsGrid;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
public class DriverRequestInfoPopUp extends VerticalLayout{

    Driver driver;

    @Autowired
    DriversRequestsGrid driversRequestsGrid;

    @Autowired
    DriversGrid driversGrid;

    @Autowired
    AdminService adminService;

    private List<Car> driverCarList;

    public void init(Driver driver) {
        this.driver = driver;
        this.driverCarList = adminService.getCarByDriver(driver);
        System.out.println(driverCarList);
        removeAllComponents();
        VerticalLayout rootLayout = new VerticalLayout();
        rootLayout.setWidthUndefined();
        rootLayout.setSpacing(true);
        rootLayout.setMargin(true);
        setTextFields(rootLayout);
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
        Label status = new Label("Status: <i>" + driver.getStringStatus(driver) + "</i>", ContentMode.HTML);
        driverForm.addComponents(name, midName, phone, email, exp, status);

        Panel driverPanel = new Panel("Personal information", driverForm);
        driverPanel.setWidth(400, Unit.PIXELS);


        VerticalLayout carForm = new VerticalLayout();
        List<Car> carList = adminService.getCarByDriver(driver);
        for (int i = 0; i < carList.size(); i++) {
            Car car = carList.get(i);
            System.out.println("CAR:" + car.getDriverId());
            Label carName = new Label("<h2><b>" + car.getMakeOfCar() + "</b></h2><hr>", ContentMode.HTML);
            Label model = new Label("Model: <i>" + car.getModelType() + "</i>", ContentMode.HTML);
            Label releaseDate = new Label("Release date: <i>" + car.getReleaseDate() + "</i>", ContentMode.HTML);
            Label seatsCount = new Label("Seats count: <i>" + car.getSeatsCount() + "</i>", ContentMode.HTML);
            Label stateNumber = new Label("State number: <i>" + car.getStateNumber() + "</i>", ContentMode.HTML);
            Label childSeat = new Label("Child seat: <i>" + car.isChildSeat() + "</i>", ContentMode.HTML);
            carForm.addComponents(carName, model, stateNumber, releaseDate, seatsCount, childSeat);
        }
        Panel carPanel = new Panel("Vehicle", carForm);
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
            List<Car> carList = adminService.getCarByDriver(driver);
            for (int i = 0; i < carList.size(); i++) {
                adminService.deleteModel(carList.get(i));
            }
            adminService.deleteModel(driver);

            driversRequestsGrid.refreshGrid();
            driversRequestsGrid.getDriversRequestSubWindow().close();
        });
        horizontalLayout.addComponent(btnDecline);

        Button btnApprove = new Button("Approve");
        btnApprove.addClickListener(clickEvent -> {
            driver.setStatus(DriverStatusValues.OFF_DUTY);
            adminService.updateModel(driver);
            driversGrid.refreshGrid();
            driversRequestsGrid.refreshGrid();

            driversRequestsGrid.getDriversRequestSubWindow().close();
        });

        horizontalLayout.addComponent(btnApprove);
        horizontalLayout.setExpandRatio(btnDecline, 0.9f);
        horizontalLayout.setExpandRatio(btnApprove, 0.1f);

        return horizontalLayout;
    }

    public List<Car> getDriverCarList() {
        return driverCarList;
    }

    public void setDriverCarList(List<Car> driverCarList) {
        this.driverCarList = driverCarList;
    }
}
