package com.netcracker.project.study.services;

import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.order.Order;

import java.math.BigInteger;

public interface ClientService {

    void registrate(Client client);

    void makeOrder(Client client, String address);

    void cancelOrder(Order order);

    void sendDriverRating(Order order, BigInteger driverRating);

}
