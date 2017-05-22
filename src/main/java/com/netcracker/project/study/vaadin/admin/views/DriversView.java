package com.netcracker.project.study.vaadin.admin.views;


import com.netcracker.project.study.vaadin.admin.components.grids.DriversGrid;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversRequestsGrid;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = DriversView.VIEW_NAME)
public class DriversView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "drivers";

    @Autowired private DriversGrid driversGrid;

    @Autowired private DriversRequestsGrid driversRequestsGrid;

    @PostConstruct
    void init() {
        TabSheet tabSheet = getTabSheet();
        addComponent(tabSheet);
    }

    private TabSheet getTabSheet() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(getAllDriversTab(), "All drivers");
        tabSheet.addTab(getDriversRequestsTab(),
                "Drivers Requests(" + driversRequestsGrid.driversRequestsList().size() + ")");
        return tabSheet;
    }

    private VerticalLayout getAllDriversTab() {
        VerticalLayout controlLayout = new VerticalLayout();
        controlLayout.setMargin(true);
        controlLayout.setSpacing(false);
        controlLayout.addComponent(driversGrid);
        return controlLayout;
    }

    private VerticalLayout getDriversRequestsTab() {
        VerticalLayout controlLayout = new VerticalLayout();
        controlLayout.addComponent(driversRequestsGrid);
        return controlLayout;
    }




    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
