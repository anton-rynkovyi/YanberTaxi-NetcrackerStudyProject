package com.netcracker.project.study.vaadin.client.views;

import com.netcracker.project.study.vaadin.client.components.ClientOrdersGrid;
import com.netcracker.project.study.vaadin.client.components.OrderMaker;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = ClientView.VIEW_NAME)
public class ClientView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "client";

    @Autowired
    private OrderMaker orderMaker;

    @Autowired
    private ClientOrdersGrid clientOrdersGrid;

    @PostConstruct
    void init() {
        TabSheet tabSheet = getTabSheet();
        addComponent(tabSheet);
    }

    private TabSheet getTabSheet() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(getOrderMakerTab(), "Make an order");
        tabSheet.addTab(getOrdersControlTab(), "Orders");

        return tabSheet;
    }

    private VerticalLayout getOrderMakerTab() {
        VerticalLayout controlLayout = new VerticalLayout();
        controlLayout.addComponent(orderMaker);

        return controlLayout;
    }

    private VerticalLayout getOrdersControlTab() {
        VerticalLayout controlLayout = new VerticalLayout();
        controlLayout.addComponent(clientOrdersGrid);

        return controlLayout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}