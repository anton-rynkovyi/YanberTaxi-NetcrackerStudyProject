package com.netcracker.project.study.vaadin.driver.components.views;

import com.netcracker.project.study.vaadin.driver.components.grids.AllOrders;
import com.netcracker.project.study.vaadin.driver.components.grids.NewOrdersGrid;
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
    private AllOrders allOrders;

    @Autowired
    private NewOrdersGrid newOrders;

    @PostConstruct
    void init() {
        TabSheet tabSheet = getTabSheet();
        addComponent(tabSheet);
    }

    private TabSheet getTabSheet() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(getAllOrdersControlTab(), "All orders ( " + allOrders.getOrdersList().size() + " )");
        tabSheet.addTab(getNewOrdersControlTab(),"New orders ( " + newOrders.getOrdersList().size() + " )");

        return tabSheet;
    }

    private VerticalLayout getAllOrdersControlTab() {
        VerticalLayout controlLayout = new VerticalLayout();
        controlLayout.addComponent(allOrders);

        return controlLayout;
    }

    private VerticalLayout getNewOrdersControlTab(){
        VerticalLayout controlLayout = new VerticalLayout();
        controlLayout.addComponent(newOrders);

        return controlLayout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
