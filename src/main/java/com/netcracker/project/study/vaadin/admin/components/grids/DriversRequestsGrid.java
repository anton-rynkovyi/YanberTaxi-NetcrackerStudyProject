package com.netcracker.project.study.vaadin.admin.components.grids;


import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusEnum;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.vaadin.admin.components.popup.DriverRequestInfoPopUp;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;

import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringComponent
@Scope(value = "prototype")
public class DriversRequestsGrid  extends CustomComponent{

    private Grid<Driver> driversRequestsGrid;

    private HorizontalLayout componentLayout;

    private List<Driver> driversRequestsList;

    private Window window;

    DriversGrid driversGrid;

    @Autowired
    AdminService adminService;

    @Autowired
    DriverService driverService;

    @Autowired
    DriverRequestInfoPopUp driverInfoPopUp;


    public void init() {
        driversRequestsGrid = generateDriversGrid();
        componentLayout = getFilledComponentLayout();
        initWindow();
        setGridSettings(driversRequestsGrid);
        setCompositionRoot(componentLayout);
    }

    private void initWindow() {
        window = new Window("Driver information");
        window.setIcon(VaadinIcons.INFO);
        window.setWidthUndefined();
        window.setHeightUndefined();
        window.center();
        window.setModal(true);
        window.setResizable(false);
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
                driverInfoPopUp.setDriversRequestsGrid(this);
                Driver driver = driversRequestsGrid.asSingleSelect().getValue();
                driverInfoPopUp.init(driver);
                UI.getCurrent().addWindow(window);
            }
        });

        return controlButtonsLayout;
    }

    private Grid<Driver> generateDriversGrid() {
        driversRequestsGrid = new Grid<>();
        driversRequestsGrid.addColumn(Driver::getLastName).setCaption("Last name");
        driversRequestsGrid.addColumn(Driver::getFirstName).setCaption("First name");
        driversRequestsGrid.addColumn(Driver::getMiddleName).setCaption("Middle name");
        driversRequestsGrid.addColumn(Driver::getEmail).setCaption("Email");
        driversRequestsGrid.addColumn(Driver::getPhoneNumber).setCaption("Phone number");
        driversRequestsGrid.addColumn(driver -> driver.getExperience() + " years").setCaption("Experience");
        driversRequestsGrid.addColumn(driver -> DriverStatusEnum.getStatusValue(driver.getStatus())).setCaption("Status");

        return driversRequestsGrid;
    }

    private void setGridSettings(Grid<Driver> driversRequestsGrid) {
        driversRequestsGrid.setSizeFull();
    }

    public Window getDriversRequestSubWindow(){
        return window;
    }

    public void refreshGrid() {
        driversRequestsList = driverService.getDriversByStatusId(DriverStatusList.APPROVAL);
        driversRequestsGrid.setItems(driversRequestsList);
    }


    public List<Driver> getDriversRequestsList() {
        return driversRequestsList;
    }

    public void setDriversRequestsList(List<Driver> driversRequestsList) {
        this.driversRequestsList = driversRequestsList;
    }

    public Grid<Driver> getDriversRequestsGrid() {return driversRequestsGrid;}

    public void setDriversGrid(DriversGrid driversGrid) {
        this.driversGrid = driversGrid;
    }
}
