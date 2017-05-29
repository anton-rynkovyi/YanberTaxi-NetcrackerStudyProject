package com.netcracker.project.study.vaadin.admin.components.grids;


import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.popup.DriverRequestInfoPopUp;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;

import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringComponent
public class DriversRequestsGrid  extends CustomComponent{

    private Grid<Driver> driversRequestsGrid;

    private HorizontalLayout componentLayout;

    private List<Driver> driversRequestsList;

    private Window window;

    @Autowired AdminService adminService;

    @Autowired DriverRequestInfoPopUp driverInfoPopUp;

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
        window.setWidthUndefined();
        window.setHeightUndefined();
        window.center();
        window.setModal(true);
        window.setContent(driverInfoPopUp);
    }

    private HorizontalLayout getFilledComponentLayout() {
        HorizontalLayout componentLayout = new HorizontalLayout();
        componentLayout.setSizeFull();

        VerticalLayout btnControlLayout = getControlButtonsLayout();
        componentLayout.addComponent(driversRequestsGrid);
        componentLayout.addComponent(btnControlLayout);

        componentLayout.setExpandRatio(driversRequestsGrid, 0.84f);
        componentLayout.setExpandRatio(btnControlLayout, 0.16f);

        return componentLayout;
    }

    private VerticalLayout getControlButtonsLayout() {
        VerticalLayout controlButtonsLayout = new VerticalLayout();
        controlButtonsLayout.setMargin(true);
        controlButtonsLayout.setSpacing(true);

        Button btnViewDriver = new Button("View driver", FontAwesome.EYE);
        controlButtonsLayout.addComponent(btnViewDriver);
        btnViewDriver.addClickListener(clickEvent -> {
            if(!driversRequestsGrid.asSingleSelect().isEmpty() ) {
                Driver driver = driversRequestsGrid.asSingleSelect().getValue();
                driverInfoPopUp.init(driver);
                UI.getCurrent().addWindow(window);
            }
        });

        return controlButtonsLayout;
    }

    private Grid<Driver> generateDriversGrid() {
        driversRequestsGrid = new Grid<>();
        refreshGrid();
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

    public Window getDriversRequestSubWindow(){
        return window;
    }

    public void refreshGrid() {
        driversRequestsList = adminService.getDriversWithApproval();
        driversRequestsGrid.setItems(driversRequestsList);
    }


    public List<Driver> getDriversRequestsList() {
        return driversRequestsList;
    }

    public void setDriversRequestsList(List<Driver> driversRequestsList) {
        this.driversRequestsList = driversRequestsList;
    }

    public Grid<Driver> getDriversRequestsGrid() {return driversRequestsGrid;}
}
