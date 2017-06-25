package com.netcracker.project.study.vaadin.admin.views;


import com.netcracker.project.study.vaadin.admin.components.grids.DriversBanGrid;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversGrid;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversRequestsGrid;
import com.netcracker.project.study.vaadin.admin.components.logo.Copyright;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringView(name = DriversView.VIEW_NAME)
public class DriversView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "drivers";

    @Autowired
    DriversGrid driversGrid;

    @Autowired DriversRequestsGrid driversRequestsGrid;

    @Autowired DriversBanGrid driversBanGrid;

    @Autowired
    Copyright bottomTeamLogoLink;

    TabSheet tabSheet;

    void init() {
        tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        setTabs();
        addComponent(tabSheet);
        addComponent(bottomTeamLogoLink);
    }

    public void setTabs() {
        tabSheet.addTab(getAllDriversTab(), "All drivers");
        tabSheet.addTab(getDriversRequestsTab(),
                "Drivers Requests(" + driversRequestsGrid.getDriversRequestsList().size() + ")");
        tabSheet.addTab(getDriversBanTab(), "Ban list");

        tabSheet.addSelectedTabChangeListener(event -> {
            driversGrid.refreshGrid();
            driversRequestsGrid.refreshGrid();
            driversBanGrid.refreshGrid();
            tabSheet.getTab(1).setCaption(
                    "Drivers Requests(" + driversRequestsGrid.getDriversRequestsList().size() + ")");
        });

    }

    private VerticalLayout getAllDriversTab() {
        VerticalLayout controlLayout = new VerticalLayout();
        controlLayout.setSizeFull();
        controlLayout.addComponent(driversGrid);
        return controlLayout;
    }

    private VerticalLayout getDriversRequestsTab() {
        VerticalLayout controlLayout = new VerticalLayout();
        controlLayout.setSizeFull();
        controlLayout.addComponent(new Label());
        controlLayout.addComponent(driversRequestsGrid);
        controlLayout.addComponent(new Label());
        return controlLayout;
    }

    private VerticalLayout getDriversBanTab() {
        VerticalLayout controlLayout = new VerticalLayout();
        controlLayout.setSizeFull();
        controlLayout.addComponent(new Label());
        controlLayout.addComponent(driversBanGrid);
        controlLayout.addComponent(new Label());
        return controlLayout;
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        driversBanGrid.init();
        driversGrid.init();
        driversRequestsGrid.init();
        driversRequestsGrid.setDriversGrid(driversGrid);
        driversRequestsGrid.refreshGrid();
        driversRequestsGrid.refreshGrid();
        driversBanGrid.refreshGrid();
        init();
    }
}
