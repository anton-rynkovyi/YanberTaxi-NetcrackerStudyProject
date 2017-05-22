package com.netcracker.project.study.vaadin.admin.components.grids;

import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import de.steinwedel.messagebox.MessageBox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.*;

@SpringComponent
public class OrdersGrid extends CustomComponent {

    @Autowired
    private PersistenceFacade facade;

    private Grid<Order> ordersGrid;

    private VerticalLayout componentLayout;

    private List<Order> ordersList;

    private Window window;

    @PostConstruct
    public void init() {
        ordersGrid = generateOrdersGrid();
        componentLayout = getFilledComponentLayout();
        setGridSettings(ordersGrid);
        setCompositionRoot(componentLayout);
    }

    private VerticalLayout getFilledComponentLayout() {
        VerticalLayout componentLayout = new VerticalLayout();
        componentLayout.setMargin(false);
        componentLayout.setSpacing(false);

        componentLayout.addComponents(ordersGrid);

        return componentLayout;
    }

    private Grid<Order> generateOrdersGrid() {

        Grid<Order> ordersGrid = new Grid<>();
        ordersList = facade.getAll(BigInteger.valueOf(Order.OBJECT_TYPE_ID), Order.class);
        ordersGrid.setItems(ordersList);

        ordersGrid.addColumn(Order::getObjectId).setCaption("№");
        ordersGrid.addColumn(Order::getDriverId).setCaption("Driver");
        ordersGrid.addColumn(Order::getClientId).setCaption("Client");
        ordersGrid.addColumn(Order::getStatus).setCaption("Status");
        ordersGrid.addColumn(Order::getCost).setCaption("Cost");
        ordersGrid.addColumn(Order::getDistance).setCaption("Distance");
        ordersGrid.addColumn(Order::getDriverRating).setCaption("Driver rating");
        ordersGrid.addColumn(Order::getDriverMemo).setCaption("Driver comment");

        return ordersGrid;
    }

    private void setGridSettings(Grid<Order> ordersGrid) {
        ordersGrid.setSizeFull();
    }

    public Grid<Order> getOrdersGrid() {
        return ordersGrid;
    }

    public List getOrdersList() {
        return ordersList;
    }

}
