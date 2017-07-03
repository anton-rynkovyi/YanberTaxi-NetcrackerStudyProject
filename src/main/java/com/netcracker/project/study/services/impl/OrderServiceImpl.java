package com.netcracker.project.study.services.impl;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.OrderAttr;
import com.netcracker.project.study.model.order.OrderStatusEnum;
import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.model.order.route.RouteAttr;
import com.netcracker.project.study.model.order.status.OrderStatus;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.OrderConstants;
import com.netcracker.project.study.services.OrderService;
import com.netcracker.project.study.vaadin.driver.components.tabs.NewOrdersTab;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    PersistenceFacade persistenceFacade;

    public static final BigDecimal COST_PER_KILOMETER = new BigDecimal("5");

    @Override
    public void calcPrice(BigInteger distance, BigInteger orderId) {
        Order order = persistenceFacade.getOne(orderId,Order.class);
        BigDecimal decimalDistance = new BigDecimal(distance);
        BigDecimal cost = decimalDistance.multiply(COST_PER_KILOMETER);
        order.setCost(cost);
        persistenceFacade.update(order);
    }


    @Override
    @Transactional
    public void changeStatus(BigInteger status, BigInteger orderId) {
        Order order = persistenceFacade.getOne(orderId,Order.class);
        order.setStatus(status);
        persistenceFacade.update(order);
        orderStatusLog(order);
    }

    @Override
    public String getStatusValue(BigInteger statusId) {
        return OrderStatusEnum.getStatusValue(statusId);
    }

    @Override
    public void setDistance(BigInteger orderId, long distance){
        Order order = persistenceFacade.getOne(orderId,Order.class);
        order.setDistance(BigInteger.valueOf(distance));
        persistenceFacade.update(order);
    }

    @Override
    public List<OrderInfo> getOrdersInfo(List<Order> orders) {
        List<OrderInfo>orderInfos = new ArrayList<>();
        int counter = 1;
        for(Order order:orders){
            if(order != null){
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setQueueN(counter++);
                orderInfo.setObjectId(order.getObjectId());
                orderInfo.setDriverName((order.getDriverId() == null)? OrderConstants.DRIVER_EMPTY:getDriverInfo(order.getDriverId()));
                orderInfo.setClientName((order.getClientId() == null)? OrderConstants.CLIENT_EMPTY:getClientInfo(order.getClientId()));
                orderInfo.setCost(getValue(order.getCost(), OrderConstants.NULL_COST) + " " + OrderConstants.CURRENCY);
                orderInfo.setDistance(getValue(order.getDistance(), OrderConstants.NULL_DISTANCE) + " " + OrderConstants.DISTANCE);
                orderInfo.setRating(order.getDriverRating());
                BigInteger status = order.getStatus();
                orderInfo.setStatus((status == null)? OrderConstants.NULL_STATUS:getStatusValue(status));
                orderInfo.setDriverMemo(order.getDriverMemo());

                List<Route> orderRoute = getRoutes(order.getObjectId());
                if(!orderRoute.isEmpty()){
                    orderInfo.setStartPoint(orderRoute.get(0).getCheckPoint());
                    orderInfo.setDestination(orderRoute.get(orderRoute.size() - 1).getCheckPoint());
                }else{
                    orderInfo.setStartPoint("---(is not set)---");
                    orderInfo.setDestination("---(is not set)---");
                }
                orderInfos.add(orderInfo);
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
        List<Order> orderList = persistenceFacade.getSome(query, Order.class,true);
        return orderList;
    }

    @Override
    public List<Order> getAllOrders(){
        String query =
                "SELECT obj.object_id " +
                        "FROM objects obj,attributes attr " +
                        "where attr.object_id=obj.object_id and " +
                        "obj.object_type_id=" + OrderAttr.OBJECT_TYPE_ID;
        List<Order> orderList = persistenceFacade.getSome(query, Order.class,true);
        return orderList;
    }

    public Order getOrder(BigInteger orderId){
        return persistenceFacade.getOne(orderId,Order.class);
    }

    @Override
    public List<Route> getRoutes(BigInteger orderId) {
       String query = "SELECT object_id " +
                      "FROM objreference " +
                      "where attr_id = " + RouteAttr.ORDER_ID_ATTR +
                      " and reference = " + orderId;

        List<Route> routeList = persistenceFacade.getSome(query, Route.class,false);
        return routeList;
    }

    private String getValue(Object value,String defaultValue){
        if(value == null){
            return defaultValue;
        }
        return  value.toString();
    }

    private String getDriverInfo(BigInteger driverId) {
        Driver driver = persistenceFacade.getOne(driverId,Driver.class);
        return driver.getLastName() + " " + driver.getFirstName();
    }

    private String getClientInfo(BigInteger clientId){
        Client client = persistenceFacade.getOne(clientId,Client.class);
        return client.getLastName() + " " + client.getFirstName();
    }


    @Override
    public List<Order> allModelsAsList() {
        List<Order> orders = persistenceFacade.getAll(BigInteger.valueOf(OrderAttr.OBJECT_TYPE_ID), Order.class);
        return orders;
    }

    @Override
    public List<Order> getOrdersByDriverId(BigInteger driverId, BigInteger orderStatusId) {
        if (orderStatusId == null) {
            String query = "SELECT obj.object_id " +
                    "FROM Objects obj " +
                    "INNER JOIN Objreference ref ON obj.object_id = ref.object_id " +
                    "WHERE obj.object_type_id = " + OrderAttr.OBJECT_TYPE_ID +
                    "AND ref.attr_id = " + OrderAttr.DRIVER_ID_ATTR +
                    "AND ref.reference = " + driverId;
            List<Order> orderList = persistenceFacade.getSome(query, Order.class,true);
            return orderList;
        }
        String query = "SELECT obj.object_id " +
        "FROM objects obj, objreference ref, attributes attr " +
        "WHERE ref.object_id=obj.object_id " +
        "AND attr.object_id=obj.object_id " +
        "AND attr.object_id=ref.object_id " +
        "AND obj.object_type_id =" + Order.OBJECT_TYPE_ID +
        " AND ref.attr_id = " + Order.DRIVER_ID_ATTR +
        " AND reference = " + driverId +
        "AND attr.attr_id = " + Order.STATUS_ATTR +
        " AND attr.list_value_id=" + orderStatusId;
        List<Order> orderList = persistenceFacade.getSome(query, Order.class,true);
        return orderList;
    }


    @Override
    public List<OrderInfo> getOrdersInfoByDriverId(BigInteger driverId, BigInteger orderStatusId) {
        return getOrdersInfo(getOrdersByDriverId(driverId,orderStatusId));
    }

    @Override
    public List<Order> getOrdersByClientId(BigInteger clientId) {
        String query = "" +
                "SELECT object_id " +
                "FROM objreference " +
                "WHERE object_type_id = " + OrderAttr.OBJECT_TYPE_ID +
                "AND attr_id = " + OrderAttr.CLIENT_ID_ATTR +
                "AND reference = " + clientId;
        List<Order> orderList = persistenceFacade.getSome(query, Order.class,true);
        return orderList;
    }

    @Override
    public List<Order> getActiveOrdersByClientId(BigInteger clientId) {
        String query = "" +
                "SELECT obj.object_id " +
                " FROM Objects obj " +
                " INNER JOIN Objreference ref ON obj.object_id = ref.object_id " +
                " INNER JOIN Attributes attr ON obj.object_id=attr.object_id "+
                " WHERE obj.object_type_id = "+ OrderAttr.OBJECT_TYPE_ID +
                " AND ref.attr_id = " + OrderAttr.CLIENT_ID_ATTR+
                " AND ref.reference = "+clientId+
                " AND attr.attr_id = 18"+
                " AND (attr.list_value_id = "+ OrderStatus.NEW+" or attr.list_value_id = "+ OrderStatus.ACCEPTED+")";
        List<Order> orderList = persistenceFacade.getSome(query, Order.class,true);
        return orderList;
    }

    @Override
    public List<Order> getPerformingOrdersByClientId(BigInteger clientId) {
        String query = "" +
                "SELECT obj.object_id " +
                " FROM Objects obj " +
                " INNER JOIN Objreference ref ON obj.object_id = ref.object_id " +
                " INNER JOIN Attributes attr ON obj.object_id=attr.object_id "+
                " WHERE obj.object_type_id = "+ OrderAttr.OBJECT_TYPE_ID +
                " AND ref.attr_id = " + OrderAttr.CLIENT_ID_ATTR+
                " AND ref.reference = "+clientId+
                " AND attr.attr_id = 18"+
                " AND attr.list_value_id = "+ OrderStatus.PERFORMING+"";
        List<Order> orderList = persistenceFacade.getSome(query, Order.class,true);
        return orderList;
    }

    @Override
    public void orderStatusLog(Order order) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(order.getObjectId());
        orderStatus.setStatus(order.getStatus());
        orderStatus.setTimeStamp(new Date(System.currentTimeMillis()));
        persistenceFacade.create(orderStatus);
    }

    @Override
    public List<Order>getCurrentOrderByDriverId(BigInteger driverId){
        String query = "SELECT obj.object_id" +
                " FROM objects obj,attributes attr, objreference ref" +
                " WHERE ref.object_id = obj.object_id AND" +
                " ref.attr_id = " + Order.DRIVER_ID_ATTR + " AND" +
                " ref.reference = " + driverId + " AND" +
                " attr.object_id = obj.object_id AND" +
                " obj.object_type_id = " + Order.OBJECT_TYPE_ID + " AND" +
                "(attr.list_value_id = " + OrderStatus.ACCEPTED + " OR " + "attr.list_value_id = " + OrderStatus.PERFORMING + ")";
        List<Order> orderList = persistenceFacade.getSome(query, Order.class,true);
        return orderList;
    }

    @Override
    public List<Order>getCurrentOrderByClientId(BigInteger clientId){
        String query = "" +
                "SELECT obj.object_id " +
                " FROM Objects obj " +
                " INNER JOIN Objreference ref ON obj.object_id = ref.object_id " +
                " INNER JOIN Attributes attr ON obj.object_id=attr.object_id "+
                " WHERE obj.object_type_id = "+ OrderAttr.OBJECT_TYPE_ID +
                " AND ref.attr_id = " + OrderAttr.CLIENT_ID_ATTR+
                " AND ref.reference = "+clientId+
                " AND attr.attr_id = 18"+
                " AND (attr.list_value_id = "+ OrderStatus.NEW+" or attr.list_value_id = "+ OrderStatus.ACCEPTED+
                " OR attr.list_value_id = "+ OrderStatus.PERFORMING+")";
        List<Order> orderList = persistenceFacade.getSome(query, Order.class,true);
        return orderList;
    }

    @Override
    public void setClientPoints(BigInteger orderId) {
        Order order = persistenceFacade.getOne(orderId, Order.class);
        Client client = order.getClientOnOrder();
        client.setPoints(client.getPoints().add(order.getDistance()));
        persistenceFacade.update(client);
    }

    @Override
    public Timestamp getLastDateFromOrdersLog(Order order) {
        String query = "select attributes.object_id " +
                " from  attributes,OBJREFERENCE " +
                " where attributes.ATTR_ID = 32 " +
                " AND OBJREFERENCE.attr_ID = 30 " +
                " AND OBJREFERENCE.REFERENCE = " + order.getObjectId() +
                " AND OBJREFERENCE.OBJECT_ID = attributes.OBJECT_ID " +
                " AND attributes.DATE_VALUE = (select MAX(attributes.date_value) " +
                " from  attributes,OBJREFERENCE " +
                " where attributes.ATTR_ID = 32 " +
                " AND OBJREFERENCE.attr_ID = 30 " +
                " AND OBJREFERENCE.REFERENCE = " + order.getObjectId() +
                " AND OBJREFERENCE.OBJECT_ID = attributes.OBJECT_ID)";
        List<OrderStatus> orderStatusList = persistenceFacade.getSome(query, OrderStatus.class,true);
        return new Timestamp(orderStatusList.get(orderStatusList.size()-1).getTimeStamp().getTime());
    }
}
