package com.netcracker.project.study.services.impl;

import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    PersistenceFacade persistenceFacade;

    public static final BigDecimal COST_PER_KILOMETER = new BigDecimal("5");

    @Override
    public void calcPrice(BigDecimal distance, Order order) {
        order.setCost(distance.multiply(COST_PER_KILOMETER));
        persistenceFacade.update(order);
    }

    @Override
    public void changeStatus(BigInteger status, Order order) {
        order.setStatus(status);
        persistenceFacade.update(order);
    }

}
