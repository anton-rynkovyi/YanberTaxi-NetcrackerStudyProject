package com.netcracker.project.study.services;

import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.vaadin.driver.components.tabs.NewOrdersTab;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;

import java.math.BigInteger;
import java.util.List;

public interface OrderService {

    void calcPrice(BigInteger distance, BigInteger orderId);

    void changeStatus(BigInteger status, BigInteger orderId);

    void setDistance(BigInteger orderId, long distance);

    String getStatusValue(BigInteger statusId);

    List<OrderInfo>getOrdersInfo(List<Order> orders);

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

    void setCommentAboutDriver(Order order, String comment);

    void setClientPoints(BigInteger orderId);

    void setDriverRating(Order order, int rating);
}
