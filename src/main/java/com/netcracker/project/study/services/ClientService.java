package com.netcracker.project.study.services;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.order.Order;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface ClientService {

    void registrate(Client client);

    void makeOrder(BigInteger clientId, BigDecimal distance, String[] addresses);

    void cancelOrder(Order order);

    void sendDriverRating(Order order, BigInteger driverRating);

    <T extends Model> List<T> allModelsAsList();
}
