package com.netcracker.project.study.vaadin.admin.components.grids;


import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.status.DriverStatusValues;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.vaadin.admin.components.popup.DriverInfoPopUp;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;

import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringComponent
public class DriversRequestsGrid  extends CustomComponent{

    private Grid<Driver> driversRequestsGrid;

    private HorizontalLayout componentLayout;

    private List<Driver> driversRequestsList;

    private Window window;

    @Autowired
    private DriversGrid driversGrid;

    @Autowired
    private DriverInfoPopUp driverInfoPopUp;

    @PostConstruct
    public void init() {
        driversRequestsGrid = generateDriversGrid();
        componentLayout = getFilledComponentLayout();
        initWindow();
        setGridSettings(driversRequestsGrid);
        setCompositionRoot(componentLayout);
    }

    private void initWindow() {
        window = new Window("Driver information");
        window.center();
        window.setContent(driverInfoPopUp);
    }

    private HorizontalLayout getFilledComponentLayout() {
        HorizontalLayout componentLayout = new HorizontalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);
        componentLayout.setSizeFull();

        VerticalLayout btnControlLayout = getControlButtonsLayout();

        componentLayout.addComponent(driversRequestsGrid);
        componentLayout.addComponent(btnControlLayout);
        componentLayout.setComponentAlignment(btnControlLayout, Alignment.TOP_RIGHT);

        return componentLayout;
    }

    private VerticalLayout getControlButtonsLayout() {
        VerticalLayout controlButtonsLayout = new VerticalLayout();
        Button btnViewDriver = new Button("View driver", FontAwesome.EYE);
        controlButtonsLayout.addComponent(btnViewDriver);
        btnViewDriver.addClickListener(clickEvent -> {
            UI.getCurrent().addWindow(window);
        });


        return controlButtonsLayout;
    }

    private Grid<Driver> generateDriversGrid() {
        Grid<Driver> driversRequestsGrid = new Grid<>();
        driversRequestsList = new ArrayList<>();

        for (int i = 0; i < driversGrid.getDriversList().size(); i++) {
            Driver driver = driversGrid.getDriversList().get(i);
            if(driver.getStatus() != null && driver.getStatus().compareTo(DriverStatusValues.APPROVAL) == 0) {
                driversRequestsList.add(driver);
            }
        }

        driversRequestsGrid.setItems(driversRequestsList);
        driversRequestsGrid.addColumn(Driver::getLastName).setCaption("Last name");
        driversRequestsGrid.addColumn(Driver::getFirstName).setCaption("First name");
        driversRequestsGrid.addColumn(Driver::getMiddleName).setCaption("Middle name");
        driversRequestsGrid.addColumn(Driver::getEmail).setCaption("Email");
        driversRequestsGrid.addColumn(Driver::getPhoneNumber).setCaption("Phone number");
        driversRequestsGrid.addColumn(driver -> driver.getExperience() + " years").setCaption("Experience");
        driversRequestsGrid.addColumn(driver -> driver.getStringStatus(driver)).setCaption("Status");

        return driversRequestsGrid;
    }

    private void setGridSettings(Grid<Driver> driversRequestsGrid) {
        driversRequestsGrid.setSizeFull();
    }

    public List<Driver> driversRequestsList() {
        return driversRequestsList;
    }

}
