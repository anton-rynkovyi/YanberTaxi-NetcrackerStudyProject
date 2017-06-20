package com.netcracker.project.study.vaadin.admin.components.grids;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.vaadin.admin.components.popup.DriverBanInfoPopUp;
import com.netcracker.project.study.vaadin.admin.components.popup.DriverInfoPopUP;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.PostConstruct;


@SpringComponent
public class DriversBanGrid extends CustomComponent{

    private Grid<Driver> driverBanGrid;

    private List<Driver> driverBanList;

    @Autowired
    AdminService adminService;

    @Autowired
    DriverService driverService;

    @Autowired
    DriverBanInfoPopUp driverBanInfoPopUp;

    private HorizontalLayout rootLayout;

    private Window driverBanInfoWindow;

    public void init() {
        initDriverBanInfoWindow();
        driverBanGrid = generateDriversGrid();
        rootLayout = generateRootlayout();
        setCompositionRoot(rootLayout);
    }

    private void initDriverBanInfoWindow() {
        driverBanInfoWindow = new Window("Driver information");
        driverBanInfoWindow.setIcon(VaadinIcons.INFO);
        driverBanInfoWindow.center();
        driverBanInfoWindow.setModal(true);
        driverBanInfoWindow.setResizable(false);
        driverBanInfoWindow.setContent(driverBanInfoPopUp);
    }

    private HorizontalLayout generateRootlayout() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.addComponent(driverBanGrid);
        horizontalLayout.setExpandRatio(driverBanGrid, 0.84f);
        VerticalLayout buttonsLayout = generateButtonsLayout();
        horizontalLayout.addComponent(buttonsLayout);
        horizontalLayout.setExpandRatio(buttonsLayout, 0.16f);
        return horizontalLayout;
    }

    private Grid<Driver> generateDriversGrid() {
        SimpleDateFormat dt = new SimpleDateFormat("dd:hh:mm");
        driverBanGrid = new Grid<>();
        driverBanGrid.setSizeFull();
        refreshGrid();
        driverBanGrid.addColumn(Driver::getObjectId).setCaption("№");
        driverBanGrid.addColumn(driver -> new Timestamp(driver.getUnbanDate().getTime())).setCaption("Unban date");
        /*driverBanGrid.addColumn(driver ->
                dt.format(new Date(driver.getUnbanDate().getTime() - System.currentTimeMillis())))
                .setCaption("Remain");*/
        driverBanGrid.addColumn(Driver::getLastName).setCaption("Last name");
        driverBanGrid.addColumn(Driver::getFirstName).setCaption("First name");
        return driverBanGrid;
    }

    private VerticalLayout generateButtonsLayout(){
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidthUndefined();
        Button btnDriverInfo = new Button("Driver info", FontAwesome.INFO);
        verticalLayout.addComponent(btnDriverInfo);
        verticalLayout.setComponentAlignment(btnDriverInfo, Alignment.BOTTOM_RIGHT);
        btnDriverInfo.addClickListener(event -> {
            if (!driverBanGrid.asSingleSelect().isEmpty()) {
                Driver driver = driverBanGrid.asSingleSelect().getValue();
                driverBanInfoPopUp.init(driver);
                UI.getCurrent().addWindow(driverBanInfoWindow);
            }
        });
        return verticalLayout;
    }

    public void refreshGrid(){
        driverBanList = driverService.getBannedDrivers();
        driverBanGrid.setItems(driverBanList);
    }

    public List<Driver> getDriverBanList(){
        return driverBanList;
    }

    public Window getDriverBanInfoWindow() {
        return driverBanInfoWindow;
    }
}
