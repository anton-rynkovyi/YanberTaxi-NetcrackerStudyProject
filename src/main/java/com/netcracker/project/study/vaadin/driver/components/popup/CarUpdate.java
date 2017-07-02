package com.netcracker.project.study.vaadin.driver.components.popup;

import com.google.common.collect.ImmutableList;
import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.facade.UserFacade;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.services.impl.UserDetailsServiceImpl;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;
import org.vaadin.spring.events.EventBus;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@SpringComponent
@Scope(value = "prototype")
public class CarUpdate extends Window {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PersistenceFacade persistenceFacade;

    @Autowired
    UserFacade userFacade;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    DriverService driverService;



    private VerticalLayout root;
    private HorizontalLayout carsLayout;
    private Toastr toastr;
    private TextField name;
    private TextField model;
    private TextField stateNumber;
    private ComboBox<Integer> prodYear;
    private  ComboBox<Integer> seatsCountsCb;
    private CheckBox childSeat;
    private ArrayList<Component> carsComponent;
    private List<Car> cars;
    private ArrayList<Integer> yearsDate;
    private ArrayList<Integer> seats;

    public void init() {
        setWindowSettings();
        VerticalLayout root = genRootLayout();
        root.addComponent(genFields());
        //root.addComponent(genButtons());
        toastr = new Toastr();
        root.addComponent(toastr);
        setContent(root);
    }




    private VerticalLayout genRootLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth(590, Unit.PIXELS);
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        return verticalLayout;
    }

    private HorizontalLayout genCarsLayout() {
        carsLayout = new HorizontalLayout();
        carsLayout.setSizeFull();
        carsLayout.setMargin(false);
        carsLayout.setSpacing(false);
        return carsLayout;
    }

    private void setWindowSettings() {
        center();
        setIcon(VaadinIcons.TAXI);
        setModal(true);
        setResizable(false);
        setCaption(" Car card");
    }



    private Component genFields() {
        Driver driver = userDetailsService.getCurrentUser();
        cars = driverService.getCarByDriver(driver);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        VerticalLayout left = new VerticalLayout();
        VerticalLayout right = new VerticalLayout();
        yearsDate = new ArrayList<>();
        seats = new ArrayList<>();
        for (int i = 2000; i < 2018; i++) {
            yearsDate.add(i);
        }
        for (int i = 2; i < 17; i++) {
            seats.add(i);
        }
        for (Car car : cars ) {
            horizontalLayout.addComponents(left, right);
            name = new TextField("Name of car");
            name.setValue(car.getMakeOfCar());
            model = new TextField("Model");
            model.setValue(car.getModelType());
            stateNumber = new TextField("State number");
            stateNumber.setValue(car.getStateNumber());
            prodYear = new ComboBox<>("Production year");
            prodYear.setItems(yearsDate);
            prodYear.setValue(Integer.parseInt(car.getReleaseDate().toString().replace("-01","")));
            seatsCountsCb = new ComboBox<>("Seats count");
            seatsCountsCb.setItems(seats);
            seatsCountsCb.setValue(car.getSeatsCount().intValue());
            childSeat = new CheckBox("Child seat");
            childSeat.setValue(car.isChildSeat());
        }
        Button ok = new Button("Ok", VaadinIcons.USER_CHECK);
        ok.setWidth(114, Unit.PIXELS);
        Button prev = new Button("Cancel", VaadinIcons.EXIT);
        prev.setWidth(114, Unit.PIXELS);

        left.addComponents(name, model, stateNumber, prev);
        right.addComponents(prodYear, seatsCountsCb, new Label(), childSeat, ok);
        right.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT);
        ok.addClickListener(event -> {
            for (Car car : cars) {
                car.setMakeOfCar(name.getValue());
                car.setModelType(model.getValue());
                car.setStateNumber(stateNumber.getValue());
                car.setReleaseDate(java.sql.Date.valueOf(prodYear.getValue() + "-01-01"));
                car.setSeatsCount(new BigInteger(String.valueOf(seatsCountsCb.getValue())));
                car.setChildSeat(childSeat.getValue() ? true : false);
                persistenceFacade.update(car);
            }
            
            close();

        });

        prev.addClickListener(event -> {
            name.clear();
            model.clear();
            stateNumber.clear();
            stateNumber.clear();
            prodYear.clear();
            seatsCountsCb.clear();
            childSeat.clear();
            close();
        });

        addCloseListener(e -> {
            prev.click();
        });

        return horizontalLayout;
    }



}
