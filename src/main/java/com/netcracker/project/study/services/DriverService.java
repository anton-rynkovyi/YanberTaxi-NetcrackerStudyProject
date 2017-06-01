package com.netcracker.project.study.services;

import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

public interface DriverService {

    void acceptOrder(BigInteger orderId, BigInteger driverId);

}
