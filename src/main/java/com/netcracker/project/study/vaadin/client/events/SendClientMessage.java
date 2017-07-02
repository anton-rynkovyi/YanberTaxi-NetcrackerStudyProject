package com.netcracker.project.study.vaadin.client.events;


import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.vaadin.driver.components.tabs.NewOrdersTab;
import com.vaadin.annotations.Push;
import com.vaadin.shared.communication.PushMode;

import java.math.BigInteger;

public class SendClientMessage {

    private final BigInteger orderId;
    private final NewOrdersTab page;
    private final Driver driver;
    private final Car car;

    public SendClientMessage (NewOrdersTab page, BigInteger orderId, Driver driver,Car car) {
        this.page = page;
        this.orderId = orderId;
        this.driver = driver;
        this.car = car;
    }

    public BigInteger getOrderId() {
        return orderId;
    }

    public NewOrdersTab getPage() {
        return page;
    }

    public Driver getDriver() {
        return driver;
    }

    public Car getCar() {
        return car;
    }
}
