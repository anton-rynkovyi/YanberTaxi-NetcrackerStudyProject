package com.netcracker.project.study.vaadin.admin.views;

import com.netcracker.project.study.vaadin.admin.components.grids.OrdersGrid;
import com.netcracker.project.study.vaadin.admin.components.logo.Copyright;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@SpringView(name = OrdersView.VIEW_NAME)
public class OrdersView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "orders";

    @Autowired
    private OrdersGrid ordersGrid;

    @Autowired
    Copyright bottomTeamLogoLink;

    @PostConstruct
    void init() {
        TabSheet tabSheet = getTabSheet();
        tabSheet.setSizeFull();
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        addComponent(tabSheet);
        addComponent(bottomTeamLogoLink);
    }

    private TabSheet getTabSheet() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.addTab(getControlTab(), "All orders");

        return tabSheet;
    }

    private VerticalLayout getControlTab() {
        VerticalLayout controlLayout = new VerticalLayout();
        controlLayout.addComponent(new Label());
        controlLayout.addComponent(ordersGrid);
        return controlLayout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
