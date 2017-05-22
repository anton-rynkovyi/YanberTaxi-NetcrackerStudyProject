package com.netcracker.project.study.vaadin.admin.components.grids;


import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.status.DriverStatusValues;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.vaadin.admin.components.popup.DriversCreatePopUp;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.*;

@SpringComponent
public class DriversGrid extends CustomComponent{

    @Autowired
    private DriversCreatePopUp driversCreatePopUp;

    @Autowired
    private PersistenceFacade facade;

    private Grid<Driver> driversGrid;

    private VerticalLayout componentLayout;

    private List<Driver> driversList;

    private List<Driver> myDrivers;

    private Window window;

    @PostConstruct
    public void init() {
        driversGrid = generateDriversGrid();
        componentLayout = getFilledComponentLayout();
        initWindow();
        setGridSettings(driversGrid);
        setCompositionRoot(componentLayout);
    }

    private VerticalLayout getFilledComponentLayout() {
        VerticalLayout componentLayout = new VerticalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);

        HorizontalLayout controlButtons = getControlButtonsLayout();

        componentLayout.addComponent(driversGrid);
        componentLayout.addComponent(controlButtons);

        return componentLayout;
    }

    private Grid<Driver> generateDriversGrid() {

        Grid<Driver> driversGrid = new Grid<>();
        driversList = facade.getAll(BigInteger.valueOf(Driver.OBJECT_TYPE_ID), Driver.class);

        myDrivers = new ArrayList<>();

        for (int i = 0; i < driversList.size(); i++) {
            if(driversList.get(i).getStatus() != DriverStatusValues.APPROVAL){
                myDrivers.add(driversList.get(i));
            }
        }

        driversGrid.setItems(myDrivers);

        driversGrid.addColumn(Driver::getObjectId).setCaption("№");
        driversGrid.addColumn(Driver::getLastName).setCaption("Last name");
        driversGrid.addColumn(Driver::getFirstName).setCaption("First name");
        driversGrid.addColumn(Driver::getMiddleName).setCaption("Middle name");
        driversGrid.addColumn(Driver::getPhoneNumber).setCaption("Phone number");
        driversGrid.addColumn(Driver::getEmail).setCaption("Email");
        driversGrid.addColumn(driver -> driver.getStringStatus(driver)).setCaption("Status");
        driversGrid.addColumn(Driver::getRating).setCaption("Rating");
        driversGrid.addColumn(Driver::getHireDate).setCaption("Hire date");
        driversGrid.addColumn(Driver::getExperience).setCaption("Exp");

        return driversGrid;
    }

    private void setGridSettings(Grid<Driver> driversGrid) {
        driversGrid.setSizeFull();
    }

    public Grid<Driver> getDriversGrid() {
        return driversGrid;
    }

    public List<Driver> getDriversList() {
        return driversList;
    }

    public List<Driver> getMyDriversList() {return myDrivers;}

    private void initWindow() {
        window = new Window("Add new driver");
        window.center();
        window.setContent(driversCreatePopUp);
    }

    public Window getDriversCreateSubWindow() {
        return window;
    }

    private HorizontalLayout getControlButtonsLayout() {
        HorizontalLayout controlButtonsLayout = new HorizontalLayout();
        controlButtonsLayout.setMargin(false);
        controlButtonsLayout.setSpacing(false);

        Button btnAddDriver = new Button("Add driver", FontAwesome.PLUS);
        controlButtonsLayout.addComponent(btnAddDriver);
        controlButtonsLayout.setComponentAlignment(btnAddDriver, Alignment.BOTTOM_LEFT);
        btnAddDriver.addClickListener(event -> {
            UI.getCurrent().addWindow(window);
        });


        Button btnDeleteDriver = new Button("Delete driver", FontAwesome.REMOVE);
        controlButtonsLayout.addComponent(btnDeleteDriver);
        controlButtonsLayout.setComponentAlignment(btnDeleteDriver, Alignment.BOTTOM_LEFT);
        btnDeleteDriver.addClickListener(event -> {
            Driver driver = driversGrid.asSingleSelect().getValue();
            String firstName = driver.getFirstName();
            String lastName = driver.getLastName();
            MessageBox
                    .createQuestion()
                    .withCaption("Delete")
                    .withMessage("Are you want to delete " + firstName + " " + lastName + "?")
                    .withYesButton(() -> {
                        facade.delete(driver.getObjectId());
                        myDrivers.remove(driver);
                        driversGrid.setItems(myDrivers);
                    })
                    .withNoButton(() -> {})
                    .open();
        });


        Button btnUpdateDriver = new Button("Update driver", FontAwesome.UPLOAD);
        controlButtonsLayout.addComponent(btnUpdateDriver);
        controlButtonsLayout.setComponentAlignment(btnUpdateDriver, Alignment.BOTTOM_LEFT);
        btnDeleteDriver.addClickListener(event -> {
          //todo realization for update button
        });


        Button btnDriverInfo = new Button("Driver info", FontAwesome.INFO);
        controlButtonsLayout.addComponent(btnDriverInfo);
        controlButtonsLayout.setComponentAlignment(btnDriverInfo, Alignment.BOTTOM_RIGHT);
        btnDriverInfo.addClickListener(event ->{});

        return controlButtonsLayout;
    }
}
