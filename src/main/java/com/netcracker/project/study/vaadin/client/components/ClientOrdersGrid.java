package com.netcracker.project.study.vaadin.client.components;

import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.services.OrderService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.List;

@SpringComponent
public class ClientOrdersGrid  extends CustomComponent {

    @Autowired
    OrderService orderService;

    private Grid<Order> clientOrderGrid;

    private List<Order> clientOrderList;

    @PostConstruct
    public void init() {
        clientOrderGrid = generateClientOrderGrid();

        setCompositionRoot(clientOrderGrid);
    }

    private Grid<Order> generateClientOrderGrid() {
        Grid<Order> ordersGrid = new Grid<>();
        ordersGrid.setSizeFull();
        clientOrderList = orderService.getOrdersByClientId(new BigInteger("88"));
        ordersGrid.setItems(clientOrderList);

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

}
