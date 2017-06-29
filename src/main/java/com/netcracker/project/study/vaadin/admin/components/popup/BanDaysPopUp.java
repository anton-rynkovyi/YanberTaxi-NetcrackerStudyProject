package com.netcracker.project.study.vaadin.admin.components.popup;

import com.google.common.eventbus.EventBus;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversGrid;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.addons.Toastr;
import org.vaadin.addons.builder.ToastBuilder;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

@SpringComponent
@Scope(value = "prototype")
public class BanDaysPopUp extends VerticalLayout {

    DriverInfoPopUP driverInfoPopUP;

    @Autowired
    AdminService adminService;

    private DriversGrid driversGrid;

    private Toastr toastr;

    private List<String> banDayList = Arrays.asList("1 min", "3 min", "5 min", "7 min");


    @PostConstruct
    private void init() {
        VerticalLayout rootLayout = generateRootLayot();
        Panel panel = generatePanel();
        rootLayout.addComponent(panel);
        toastr = new Toastr();
        rootLayout.addComponent(toastr);

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
            Driver driver = adminService.getModelById(driverInfoPopUP.getDriver().getObjectId(), Driver.class);
            adminService.giveBan(driver, days);
            driversGrid.refreshGrid();
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

    public void setDriversGrid(DriversGrid driversGrid) {
        this.driversGrid = driversGrid;
    }

    public void setDriverInfoPopUP(DriverInfoPopUP driverInfoPopUP) {
        this.driverInfoPopUP = driverInfoPopUP;
    }
}
