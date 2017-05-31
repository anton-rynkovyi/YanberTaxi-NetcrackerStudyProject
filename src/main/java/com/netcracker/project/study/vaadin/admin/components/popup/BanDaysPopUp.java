package com.netcracker.project.study.vaadin.admin.components.popup;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversBanGrid;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversGrid;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

@SpringComponent
public class BanDaysPopUp extends VerticalLayout {

    @Autowired
    DriverInfoPopUP driverInfoPopUP;

    @Autowired
    AdminService adminService;

    @Autowired
    DriversGrid driversGrid;

    @Autowired
    DriversBanGrid driversBanGrid;

    private List<String> banDayList = Arrays.asList("1 min", "3 min", "5 min", "7 min");

    @PostConstruct
    private void init() {
        VerticalLayout rootLayout = generateRootLayot();
        Panel panel = generatePanel();
        rootLayout.addComponent(panel);
        addComponent(rootLayout);
    }

    private Panel generatePanel() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(true);
        RadioButtonGroup radioButtonGroup = generateRadioButtons();
        verticalLayout.addComponent(radioButtonGroup);
        verticalLayout.addComponent(generateControlButtons(radioButtonGroup));
        Panel panel = new Panel();
        panel.setContent(verticalLayout);
        return panel;
    }

    private HorizontalLayout generateControlButtons(RadioButtonGroup radioButtonGroup) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button btnOk = new Button("Ok");
        horizontalLayout.addComponent(btnOk);
        btnOk.addClickListener(clickEvent -> {
            String radioValue[] = String.valueOf(radioButtonGroup.getValue()).split(" ");
            int days = Integer.parseInt(radioValue[0]);
            Driver driver = driverInfoPopUP.getDriver();
            adminService.giveBan(driver, days);
            driversGrid.getApprovedDriversList().remove(driver);
            driversGrid.refreshGrid();
            driversBanGrid.getDriverBanList().add(driver);
            driversBanGrid.refreshGrid();
            driverInfoPopUP.getBanDaysWindow().close();
        });


        Button btnCancel = new Button("Cancel");
        horizontalLayout.addComponent(btnCancel);
        btnCancel.addClickListener(clickEvent -> {
            driverInfoPopUP.getBanDaysWindow().close();
        });


        return horizontalLayout;
    }

    private VerticalLayout generateRootLayot() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        verticalLayout.setSpacing(true);

        return verticalLayout;
    }

    private RadioButtonGroup generateRadioButtons() {
        RadioButtonGroup radioButtonGroup = new RadioButtonGroup<>("For how many days?");
        radioButtonGroup.setItems(banDayList);

        return radioButtonGroup;
    }
}
