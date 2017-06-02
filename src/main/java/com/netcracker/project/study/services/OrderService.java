package com.netcracker.project.study.services;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.order.Order;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;

import java.util.List;

public interface OrderService {

    void calcPrice(BigDecimal distance, Order order);

    void changeStatus(BigInteger status, Order order);

    String getStatusValue(BigInteger statusId);

    List<OrderInfo>getOrdersInfo(List<Order>orders);

    List<Order> getOrders(BigInteger statusId);

    List<Route> getRoutes(BigInteger orderId);

    List<Order> allModelsAsList();
}
