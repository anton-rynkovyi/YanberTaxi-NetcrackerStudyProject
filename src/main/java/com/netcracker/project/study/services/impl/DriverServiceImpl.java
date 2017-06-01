package com.netcracker.project.study.services.impl;

import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    PersistenceFacade facade;

    @Override
    public void acceptOrder(BigInteger orderId, BigInteger driverId) {
        Order order = facade.getOne(orderId,Order.class);
        if(order.getStatus().equals(Order.NEW)){
            order.setDriverId(driverId);
            order.setStatus(Order.ACCEPTED);
            facade.update(order);
        }else{
            throw new RuntimeException("The order must have status equals to new");
        }

    }
}
