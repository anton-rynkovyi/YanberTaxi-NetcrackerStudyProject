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

    void changeStatus(BigInteger status, BigInteger orderId);

    void setDistance(BigInteger orderId, long distance);

    String getStatusValue(BigInteger statusId);

    List<OrderInfo>getOrdersInfo(List<Order>orders);

    List<Order> getOrders(BigInteger statusId);

    List<Route> getRoutes(BigInteger orderId);

    List<Order> allModelsAsList();

    List<Order> getOrdersByDriverId(BigInteger driverId, BigInteger orderStatusId);

    List<Order> getOrdersByClientId(BigInteger clientId);

    void orderStatusLog(Order order);

    List<Order> getActiveOrdersByClientId(BigInteger clientId);

    List<Order> getPerformingOrdersByClientId(BigInteger clientId);

    List<Order>getCurrentOrderByDriverId(BigInteger driverId);

    List<Order>getCurrentOrderByClientId(BigInteger clientId);

    Order getOrder(BigInteger orderId);

    List<OrderInfo> getOrdersInfoByDriverId(BigInteger driverId, BigInteger orderStatusId);
}
