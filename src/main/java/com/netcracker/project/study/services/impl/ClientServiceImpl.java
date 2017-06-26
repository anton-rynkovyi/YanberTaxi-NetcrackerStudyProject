package com.netcracker.project.study.services.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.client.ClientAttr;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.route.Route;
import com.netcracker.project.study.model.order.status.OrderStatus;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.ClientService;
import com.netcracker.project.study.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    PersistenceFacade persistenceFacade;

    @Autowired
    OrderService orderService;

    @Override
    public void registrate(Client client) {
        persistenceFacade.create(client);
    }

    @Transactional
    @Override
    public void makeOrder(BigInteger clientId, String[] addresses) {
        Order order = new Order();
        order.setClientId(clientId);
        order.setStatus(OrderStatus.NEW);
        order.setName(addresses[0] + " - " + addresses[4]);

        persistenceFacade.create(order);

        int count = 1;
        for (int i = 0; i < addresses.length; i++) {
            if (addresses[i] != null) {
                Route route = new Route(addresses[i]);
                route.setOrderId(order.getObjectId());
                route.setCheckPoint(addresses[i]);
                route.setShowOrder(BigInteger.valueOf(count));

                persistenceFacade.create(route);

                count++;
            }
        }
    }

    @Override
    public void cancelOrder(Order order) {
        orderService.changeStatus(OrderStatus.CANCELED,order.getObjectId());

        //if not succeed transfer Order parameter and parameter will be Client
        /*String sqlQuery = "SELECT obj.object_id "+
                "FROM Objects obj " +
                "inner join OBJREFERENCE obj_r" +
                " on obj.object_id=obj_r.OBJECT_ID " +
                "inner join ATTRIBUTES attr " +
                "on obj.OBJECT_ID=attr.OBJECT_ID "+
                "WHERE "+
                "obj.object_type_id = 3 "+
                "and obj_r.REFERENCE= "+client.getObjectId().toString()+" "+
                "and obj_r.attr_id=16 " +
                "and attr.attr_id = 18 "+
                "and (attr.list_value_id = 6 or attr.list_value_id = 7 or attr.list_value_id = 8)";
        List<Order> orders =  persistenceFacade.getSome(sqlQuery, Order.class);
        for (Order order:orders) {
           orderService.changeStatus(Order.CANCELED,order);
        }*/
    }

    @Transactional
    @Override
    public void sendDriverRating(Order order, BigInteger driverRating) {
        order.setDriverRating(driverRating);
        persistenceFacade.update(order);
        calcDriverRating(persistenceFacade.getOne(order.getDriverId(),Driver.class));
    }

    @Override
    public <T extends Model> List<T> allModelsAsList() {
        List<T> clients = persistenceFacade.getAll(BigInteger.valueOf(ClientAttr.OBJECT_TYPE_ID), Client.class);
        return clients;
    }

    private void calcDriverRating(Driver driver) {
        String sqlQuery = "SELECT obj.object_id "+
                "FROM Objects obj  "+
                "inner join OBJREFERENCE obj_r "+
                "on obj.object_id=obj_r.OBJECT_ID "+
                "WHERE "+
                "obj.object_type_id = 3 "+
                "and obj_r.REFERENCE= "+driver.getObjectId().toString()+" "+
                "and obj_r.attr_id=17 ";
        List<Order> orders = persistenceFacade.getSome(sqlQuery,Order.class);
        BigInteger rating = BigInteger.valueOf(0);
        BigInteger quantity = BigInteger.valueOf(0);
        for (Order order:orders) {
            rating.add(order.getDriverRating());
            quantity.add(BigInteger.valueOf(1));
        }
        driver.setRating(new BigDecimal(rating.divide(quantity)));
        persistenceFacade.update(driver);
    }



}