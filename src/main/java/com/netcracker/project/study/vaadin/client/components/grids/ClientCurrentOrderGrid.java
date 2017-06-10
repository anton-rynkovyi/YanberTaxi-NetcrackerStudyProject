package com.netcracker.project.study.vaadin.client.components.grids;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.OrderStatusEnum;
import com.netcracker.project.study.services.OrderService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.List;

@SpringComponent
public class ClientCurrentOrderGrid extends CustomComponent {

    @Autowired
    OrderService orderService;

    Client client;

    private Grid<Order> clientOrderGrid;

    @PostConstruct
    public void init() {
        clientOrderGrid = currentOrder();
        setCompositionRoot(clientOrderGrid);
    }

    private Grid<Order> currentOrder() {
        Grid<Order> currentOrderInfo = new Grid<>();
        currentOrderInfo.setSizeFull();

        List<Order> clientCurrentOrder = orderService.getActiveOrdersByClientId(BigInteger.valueOf(242));
        orderService.getOrdersInfo(clientCurrentOrder);
        currentOrderInfo.setItems(clientCurrentOrder);

        currentOrderInfo.addColumn(Order::getObjectId).setCaption("№");
        currentOrderInfo.addColumn(Order -> Order.getName().substring(0, Order.getName().indexOf(" "))).setCaption("Start point");
        currentOrderInfo.addColumn(Order -> Order.getName().substring((Order.getName().indexOf("- "))+1)).setCaption("Destination");
        currentOrderInfo.addColumn(Order -> Order.getDriverOnOrder() != null ? Order.getDriverOnOrder().getFirstName() + " " +
                Order.getDriverOnOrder().getLastName() : "").setCaption("Driver");
        currentOrderInfo.addColumn(order -> OrderStatusEnum.getStatusValue(order.getStatus())).setCaption("Status");

        return currentOrderInfo;
    }

    public void setClient(Client client) {this.client = client;}
}
