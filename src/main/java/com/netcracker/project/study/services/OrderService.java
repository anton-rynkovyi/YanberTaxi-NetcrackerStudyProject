package com.netcracker.project.study.services;

import com.netcracker.project.study.model.order.Order;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface OrderService {

    void calcPrice(BigDecimal distance, Order order);

    void changeStatus(BigInteger status, Order order);
}
