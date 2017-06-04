package com.netcracker.project.study.services.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.client.ClientAttr;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    PersistenceFacade persistenceFacade;

    @Autowired
    AdminService adminService;


    @Override
    public void acceptOrder(BigInteger orderId, BigInteger driverId) {
        Order order = persistenceFacade.getOne(orderId,Order.class);
        if(order.getStatus().equals(Order.NEW)){
            order.setDriverId(driverId);
            order.setStatus(Order.ACCEPTED);
            persistenceFacade.update(order);
        }else{
            throw new RuntimeException("The order must have status equals to new");
        }

    }

    @Override
    public <T extends Model> List<T> allModelsAsList() {
        List<T> clients = persistenceFacade.getAll(BigInteger.valueOf(DriverAttr.OBJECT_TYPE_ID), Driver.class);
        return clients;
    }



}
