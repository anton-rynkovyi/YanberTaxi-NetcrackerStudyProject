package com.netcracker.project.study.vaadin.driver.components.tabs;

import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import com.vaadin.ui.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class OrderGrid extends Grid<OrderInfo> {

    @Autowired
    OrderService orderService;

    List<Order> allOrdersList;


    public void init(Driver driver) {
        setSizeFull();
        allOrdersList = orderService.getOrdersByDriverId(driver.getObjectId(), null);
        List<OrderInfo> ordersInfo = orderService.getOrdersInfo(allOrdersList);

        setItems(ordersInfo);

        addColumn(OrderInfo::getQueueN).setCaption("#");
        addColumn(OrderInfo::getClientName).setCaption("Client");
        addColumn(OrderInfo::getStatus).setCaption("Status");
        addColumn(OrderInfo::getCost).setCaption("Cost");
        addColumn(OrderInfo::getDistance).setCaption("Distance");
    }
}
