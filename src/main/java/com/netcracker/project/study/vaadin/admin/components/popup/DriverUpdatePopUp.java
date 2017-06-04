package com.netcracker.project.study.vaadin.admin.components.popup;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversGrid;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversRequestsGrid;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

@SpringComponent
public class DriverUpdatePopUp extends VerticalLayout {

    @Autowired AdminService adminService;

    @Autowired DriversGrid driversGrid;

    @Autowired DriversRequestsGrid driversRequestsGrid;

    private Driver driver;

    private List<Car> carList;

    private String[] childSeatItems = new String[]{"Yes", "No"};


    public void init(Driver driver) {
        this.driver = driver;
        removeAllComponents();
        VerticalLayout layout = new VerticalLayout();
        layout.setWidthUndefined();
        layout.setSpacing(true);
        layout.setMargin(true);
        setTextFields(layout);
        addComponent(layout);
    }

    public void setTextFields(VerticalLayout layout) {
        HorizontalLayout layoutForForms = new HorizontalLayout();
        FormLayout driverForm = new FormLayout();
        driverForm.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        driverForm.setWidth(450, Unit.PIXELS);

        TextField lastName = new TextField("Last Name",
                driver.getLastName() == null ? "" : driver.getLastName());
        lastName.setIcon(FontAwesome.USER);
        lastName.beforeClientResponse(false);
        driverForm.addComponent(lastName);

        TextField firstName = new TextField("First Name",
                driver.getFirstName() == null ? "" : driver.getFirstName());
        firstName.setIcon(FontAwesome.USER);
        lastName.beforeClientResponse(true);
        driverForm.addComponent(firstName);

        TextField middleName = new TextField("Middle Name",
                driver.getMiddleName() == null ? "" : driver.getMiddleName());
        middleName.setIcon(FontAwesome.USER);
        driverForm.addComponent(middleName);

        TextField phoneNumber = new TextField("Phone number",
                driver.getPhoneNumber() == null ? "" : driver.getPhoneNumber());
        phoneNumber.setIcon(FontAwesome.MOBILE_PHONE);
        driverForm.addComponent(phoneNumber);

        TextField email = new TextField("Email address",
                driver.getEmail() == null ? "" : driver.getEmail());
        email.setIcon(FontAwesome.ENVELOPE);
        driverForm.addComponent(email);

        TextField experience = new TextField("Experience",
                driver.getExperience()  == null ? "" : String.valueOf(driver.getExperience()));
        experience.setIcon(FontAwesome.LONG_ARROW_UP);
        driverForm.addComponent(experience);

        TextField status = new TextField("status",
                driver.getStatus() == null ? "" : String.valueOf(driver.getStatus()));
        experience.setIcon(FontAwesome.LONG_ARROW_UP);
        driverForm.addComponent(status);

        carList = adminService.getCarByDriver(driver);
        FormLayout carForm = new FormLayout();
        carForm.setDefaultComponentAlignment(Alignment.TOP_CENTER);
        carForm.setWidth(450, Unit.PIXELS);

        TextField[]
                carName = new TextField[carList.size()],
                model = new TextField[carList.size()],
                stateNumber = new TextField[carList.size()],
                releaseDate = new TextField[carList.size()],
                seatsCount = new TextField[carList.size()],
                childSeat = new TextField[carList.size()];
        ComboBox childSeats[] = new ComboBox[carList.size()];

        for (int i = 0; i < carList.size(); i++) {
            Car car = carList.get(0);
            carName[i] = new TextField("Car name",
                    car.getMakeOfCar() != null ? car.getMakeOfCar() : "");
            carName[i].setIcon(VaadinIcons.CAR);
            carName[i].beforeClientResponse(false);
            carForm.addComponent(carName[i]);

            model[i] = new TextField("Model",
                    car.getModelType() != null ? car.getModelType() : "");
            model[i].setIcon(VaadinIcons.CAR);
            model[i].beforeClientResponse(false);
            carForm.addComponent(model[i]);

            stateNumber[i] = new TextField("State number",
                    car.getStateNumber() != null ? car.getStateNumber() : "");
            stateNumber[i].setIcon(VaadinIcons.CAR);
            stateNumber[i].beforeClientResponse(false);
            carForm.addComponent(stateNumber[i]);

            releaseDate[i] = new TextField("Release date",
                   car.getReleaseDate() != null ?  String.valueOf(car.getReleaseDate()) : "");
            releaseDate[i].setIcon(VaadinIcons.CAR);
            releaseDate[i].beforeClientResponse(false);
            carForm.addComponent(releaseDate[i]);

            seatsCount[i] = new TextField("Seats count",
                    car.getSeatsCount() == null ? "" : String.valueOf(car.getSeatsCount()));
            seatsCount[i].setIcon(VaadinIcons.CAR);
            seatsCount[i].beforeClientResponse(false);
            carForm.addComponent(seatsCount[i]);

            /*childSeat[i] = new TextField("Child seat", String.valueOf(car.isChildSeat()));
            childSeat[i].setIcon(VaadinIcons.CAR);
            childSeat[i].beforeClientResponse(false);
            carForm.addComponent(childSeat[i]);*/
            childSeats[i] = new ComboBox("Child seat");
            childSeats[i].setItems(childSeatItems);
            if(car.isChildSeat() == true) {
                childSeats[i].setValue(childSeatItems[0]);
            }else {
                childSeats[i].setValue(childSeatItems[1]);
            }
            childSeats[i].setIcon(VaadinIcons.CAR);
            childSeats[i].beforeClientResponse(false);
            carForm.addComponent(childSeats[i]);
        }

        Panel driverPanel = new Panel("Driver", driverForm);
        Panel carPanel = new Panel("Car", carForm);
        layoutForForms.addComponent(driverPanel);
        layoutForForms.addComponent(carPanel);
        layout.addComponent(layoutForForms);

        Button btnAddToDB = new Button("Update", VaadinIcons.UPLOAD);
        btnAddToDB.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {

                driver.setName(lastName.getValue() +" " + firstName.getValue());
                driver.setLastName(!lastName.getValue().equals("") ? lastName.getValue() : null);
                driver.setFirstName(!firstName.getValue().equals("") ? firstName.getValue() : null);
                driver.setMiddleName(!middleName.getValue().equals("") ? middleName.getValue() : null);
                driver.setPhoneNumber(!phoneNumber.getValue().equals("") ? phoneNumber.getValue() : null);
                driver.setEmail(!email.getValue().equals("") ? email.getValue() : null);
                driver.setHireDate(new Date(System.currentTimeMillis()));
                driver.setExperience(!experience.getValue().equals("")
                        ? BigInteger.valueOf(Long.parseLong(experience.getValue())) : null);
                driver.setRating(new BigDecimal(4));
                driver.setStatus(new BigInteger(status.getValue()));

                adminService.updateModel(driver);

                driversGrid.refreshGrid();
                driversRequestsGrid.refreshGrid();

                lastName.setValue("");
                firstName.setValue("");
                middleName.setValue("");
                phoneNumber.setValue("");
                email.setValue("");
                experience.setValue("");
                status.setValue("");

                for (int i = 0; i < carList.size(); i++) {
                    Car car = carList.get(i);
                    car.setMakeOfCar(!carName[i].getValue().equals("") ? carName[i].getValue() : null);
                    car.setModelType(!model[i].getValue().equals("") ? model[i].getValue() : null);
                    car.setStateNumber(!stateNumber[i].getValue().equals("") ? stateNumber[i].getValue() : null);
                    car.setReleaseDate(!releaseDate[i].getValue().equals("") ? Date.valueOf(releaseDate[i].getValue()) : null);
                    car.setSeatsCount(!seatsCount[i].getValue().equals("") ? new BigInteger(seatsCount[i].getValue()) : null);
                    //car.setChildSeat(Boolean.parseBoolean(childSeat[i].getValue()));
                    if(childSeats[i].getValue().equals(childSeatItems[0])){
                        car.setChildSeat(true);
                    } else {
                        car.setChildSeat(false);
                    }

                    adminService.updateModel(car);

                    carName[i].setValue("");
                    model[i].setValue("");
                    stateNumber[i].setValue("");
                    releaseDate[i].setValue("");
                    seatsCount[i].setValue("");
                    //childSeat[i].setValue("");
                }

                /*Toastr successToast = new Toastr();
                successToast.toast(ToastBuilder.success("The driver has been successfully approved").build());*/
                //driversGrid.getApprovedDriversList().add(driver);


                driversGrid.getUpdateDriverWindow().close();

                //AdminPage.navigator.navigateTo(DriversView.VIEW_NAME);
            }
        });

        layout.addComponent(btnAddToDB);
    }

}
