package com.netcracker.project.study.services.impl;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.ClientService;
import com.netcracker.project.study.services.OrderService;
import com.vaadin.event.dd.acceptcriteria.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class ClientServiceImpl implements ClientService {

    @Autowired
    PersistenceFacade persistenceFacade;

    @Autowired
    OrderService orderService;

    @Override
    public void registrate(Client client) {
        persistenceFacade.create(client);
    }

    @Override
    public void makeOrder(Client client, String address) {
        Order order = new Order();
        order.setClientId(client.getObjectId());
        order.setStatus(Order.NEW);
        order.setName(address);
        persistenceFacade.create(order);
    }

    @Override
    public void cancelOrder(Order order) {
        orderService.changeStatus(Order.CANCELED,order);

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

    @Override
    public void sendDriverRating(Order order, BigInteger driverRating) {
        order.setDriverRating(driverRating);
        persistenceFacade.update(order);
        calcDriverRating(persistenceFacade.getOne(order.getDriverId(),Driver.class));
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
