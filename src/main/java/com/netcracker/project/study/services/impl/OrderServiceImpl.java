package com.netcracker.project.study.services.impl;


import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.OrderAttr;
import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.model.order.route.RouteAttr;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.OrderConstants;
import com.netcracker.project.study.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    PersistenceFacade facade;

    public static final BigDecimal COST_PER_KILOMETER = new BigDecimal("5");

    @Override
    public void calcPrice(BigDecimal distance, Order order) {
        order.setCost(distance.multiply(COST_PER_KILOMETER));
        facade.update(order);
    }

    @Override
    public void changeStatus(BigInteger status, Order order) {
        order.setStatus(status);
        facade.update(order);
    }

    @Override
    public String getStatusValue(BigInteger statusId) {
        if(statusId.equals(Order.NEW)){
            return "New";
        }else if(statusId.equals(Order.ACCEPTED)){
            return "Accepted";
        }else if(statusId.equals(Order.PERFORMED)){
            return "Performed";
        }else if(statusId.equals(Order.PERFORMING)){
            return "Performing";
        }else if(statusId.equals(Order.CANCELED)){
            return "Canceled";
        }
        return OrderConstants.NULL_STATUS;
    }

    @Override
    public List<OrderInfo> getOrdersInfo(List<Order> orders) {
        List<OrderInfo>orderInfos = new ArrayList<>();
        OrderInfo temp;
        for(Order order:orders){
            if(order != null){
                temp = new OrderInfo();
                temp.setObjectId(order.getObjectId());
                temp.setDriverName((order.getDriverId() == null)?OrderConstants.DRIVER_EMPTY:getDriverInfo(order.getDriverId()));
                temp.setClientName((order.getClientId() == null)?OrderConstants.CLIENT_EMPTY:getClientInfo(order.getClientId()));
                temp.setCost(getValue(order.getCost(),OrderConstants.NULL_COST) + " " + OrderConstants.CURRENCY);
                temp.setDistance(getValue(order.getDistance(),OrderConstants.NULL_DISTANCE) + " " + OrderConstants.DISTANCE);
                BigInteger status = order.getStatus();
                temp.setStatus((status == null)?OrderConstants.NULL_STATUS:getStatusValue(order.getStatus()));

                orderInfos.add(temp);
            }
        }

        return orderInfos;
    }

    @Override
    public List<Order> getOrders(BigInteger statusId){
        String query =
                "SELECT obj.object_id " +
                        "FROM objects obj,attributes attr " +
                        "where attr.object_id=obj.object_id and " +
                        "obj.object_type_id=" + OrderAttr.OBJECT_TYPE_ID +
                        " and attr.list_value_id=" + statusId;
        List<Order> orderList = facade.getSome(query, Order.class);
        return orderList;
    }

    @Override
    public List<Route> getRoutes(BigInteger orderId) {
       String query = "SELECT object_id " +
                      "FROM objreference " +
                      "where attr_id = " + RouteAttr.ORDER_ID_ATTR +
                      " and reference = " + orderId;

        List<Route> routeList = facade.getSome(query, Route.class);
        return routeList;
    }

    private String getValue(Object value,String defaultValue){
        if(value == null){
            return defaultValue;
        }
        return  value.toString();
    }

    private String getDriverInfo(BigInteger driverId) {
        Driver driver = facade.getOne(driverId,Driver.class);
        return driver.getLastName() + " " + driver.getFirstName();
    }

    private String getClientInfo(BigInteger clientId){
        Client client = facade.getOne(clientId,Client.class);
        return client.getLastName() + " " + client.getFirstName();
    }
}
