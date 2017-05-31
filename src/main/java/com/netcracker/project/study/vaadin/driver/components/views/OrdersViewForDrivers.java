package com.netcracker.project.study.vaadin.driver.components.views;

import com.netcracker.project.study.vaadin.admin.components.grids.OrdersGrid;
import com.netcracker.project.study.vaadin.driver.components.grids.OrderGridForDrivers;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = OrdersViewForDrivers.VIEW_NAME)
public class OrdersViewForDrivers extends VerticalLayout implements View {

    public static final String VIEW_NAME = "ordersForDriverPage";

    @Autowired
    private OrdersGrid ordersGrid;

    @Autowired
    private OrderGridForDrivers orderGridForDrivers;


    @PostConstruct
    void init() {
        TabSheet tabSheet = getTabSheet();
        addComponent(tabSheet);
    }

    private TabSheet getTabSheet() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(getControlTab(), "Control");

        return tabSheet;
    }

    private VerticalLayout getControlTab() {
        VerticalLayout controlLayout = new VerticalLayout();
        //controlLayout.addComponent(ordersGrid);
        controlLayout.addComponent(orderGridForDrivers);

        return controlLayout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
