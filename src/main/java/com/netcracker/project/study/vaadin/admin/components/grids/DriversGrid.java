package com.netcracker.project.study.vaadin.admin.components.grids;


import com.netcracker.project.study.model.driver.Driver;

import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.popup.DriverInfoPopUP;
import com.netcracker.project.study.vaadin.admin.components.popup.DriverCreatePopUp;
import com.netcracker.project.study.vaadin.admin.components.popup.DriverUpdatePopUp;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;

@SpringComponent
public class DriversGrid extends CustomComponent{

    @Autowired AdminService adminService;

    @Autowired DriverCreatePopUp driverCreatePopUp;

    @Autowired DriverInfoPopUP driverInfoPopUp;

    @Autowired DriverUpdatePopUp driverUpdatePopUp;

    private Grid<Driver> driversGrid;

    private VerticalLayout componentLayout;

    private List<Driver> driversList;

    private Window createDriverWindow;

    private Window driverInfoWindow;

    private Window updateDriverWindow;



    @PostConstruct
    public void init() {
        driversGrid = generateDriversGrid();
        componentLayout = getFilledComponentLayout();
        initCreateDriverWindow();
        initDriverInfoWindow();
        initUpdateInfoWindow();
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

        driversGrid = new Grid<>();
        refreshGrid();
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

    private void initCreateDriverWindow() {
        createDriverWindow = new Window("Add new driver");
        createDriverWindow.center();
        createDriverWindow.setModal(true);
        createDriverWindow.setContent(driverCreatePopUp);
    }

    private void initDriverInfoWindow() {
        driverInfoWindow = new Window("Driver information");
        driverInfoWindow.center();
        driverInfoWindow.setModal(true);
        driverInfoWindow.setContent(driverInfoPopUp);
    }

    private void initUpdateInfoWindow() {
        updateDriverWindow = new Window("Update driver");
        updateDriverWindow.center();
        updateDriverWindow.setModal(true);
        updateDriverWindow.setContent(driverUpdatePopUp);
    }

    public Window getDriversCreateSubWindow() {
        return createDriverWindow;
    }

    private HorizontalLayout getControlButtonsLayout() {
        HorizontalLayout controlButtonsLayout = new HorizontalLayout();
        controlButtonsLayout.setMargin(false);
        controlButtonsLayout.setSpacing(false);

        Button btnAddDriver = new Button("Add driver", FontAwesome.PLUS);
        controlButtonsLayout.addComponent(btnAddDriver);
        controlButtonsLayout.setComponentAlignment(btnAddDriver, Alignment.BOTTOM_LEFT);
        btnAddDriver.addClickListener(event -> {
            UI.getCurrent().addWindow(createDriverWindow);
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
                        adminService.deleteModel(driver);
/*
                        driversList.remove(driver);
                        driversGrid.setItems(driversList);
*/
                    refreshGrid();
                    })
                    .withNoButton(() -> {})
                    .open();
        });


        Button btnUpdateDriver = new Button("Update driver", FontAwesome.UPLOAD);
        controlButtonsLayout.addComponent(btnUpdateDriver);
        controlButtonsLayout.setComponentAlignment(btnUpdateDriver, Alignment.BOTTOM_LEFT);
        btnUpdateDriver.addClickListener(event -> {
            if(!driversGrid.asSingleSelect().isEmpty() ) {
                Driver driver = driversGrid.asSingleSelect().getValue();
                driverUpdatePopUp.init(driver);
                UI.getCurrent().addWindow(updateDriverWindow);
                updateDriverWindow.center();
            }
          //todo realization for update button
        });


        Button btnDriverInfo = new Button("Driver info", FontAwesome.INFO);
        controlButtonsLayout.addComponent(btnDriverInfo);
        controlButtonsLayout.setComponentAlignment(btnDriverInfo, Alignment.BOTTOM_RIGHT);
        btnDriverInfo.addClickListener(event ->{
            if(!driversGrid.asSingleSelect().isEmpty() ) {
                Driver driver = driversGrid.asSingleSelect().getValue();
                driverInfoPopUp.init(driver);
                UI.getCurrent().addWindow(driverInfoWindow);
            }
        });

        return controlButtonsLayout;
    }

    public void refreshGrid(){
        driversList = adminService.getActiveDrivers();
        driversGrid.setItems(driversList);
    }


    public Grid<Driver> getDriversGrid() {
        return driversGrid;
    }

    public List<Driver> getApprovedDriversList() {
        return driversList;
    }

    public void setDriversList(List<Driver> driversList) {
        this.driversList = driversList;
    }
}
