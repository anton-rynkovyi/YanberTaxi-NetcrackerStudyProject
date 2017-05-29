package com.netcracker.project.study.vaadin.admin.components.grids;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.services.AdminService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import javax.annotation.PostConstruct;

@SpringComponent
public class DriversBanGrid extends CustomComponent{

    private Grid<Driver> driverBanGrid;

    private List<Driver> driverBanList;

    @Autowired
    AdminService adminService;

    @Autowired
    DriversGrid driversGrid;
/*
    @Autowired
    DriversBanGrid driversBanGrid;*/

    @PostConstruct
    private void init() {
        driverBanGrid = generateDriversGrid();
        setCompositionRoot(driverBanGrid);
    }


    private Grid<Driver> generateDriversGrid() {
        driverBanGrid = new Grid<>();
        refreshGrid();
        driverBanGrid.addColumn(Driver::getObjectId).setCaption("№");
        driverBanGrid.addColumn(Driver::getUnbanDate).setCaption("Unban date");
        driverBanGrid.addColumn(Driver::getLastName).setCaption("Last name");
        driverBanGrid.addColumn(Driver::getFirstName).setCaption("First name");
        return driverBanGrid;
    }

    public void refreshGrid(){
        driverBanList = adminService.getBannedDrivers();
        driverBanGrid.setItems(driverBanList);
    }


}
