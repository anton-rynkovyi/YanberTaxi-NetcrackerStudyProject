package com.netcracker.project.study.services;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.vaadin.driver.pojos.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

public interface DriverService {

    void acceptOrder(BigInteger orderId, BigInteger driverId);

    <T extends Model> List<T> allModelsAsList();

    List<Driver> getDriversByStatusId(BigInteger statusId);

    List<Driver> getActiveDrivers();

    List<Car> getCarByDriver(Driver driver);

    List<Driver> getBannedDrivers();

    void driverStatusLog(Driver driver);

    void changeStatus(BigInteger status, Driver driver);

    void changeStatus(BigInteger status, BigInteger driverId);


}
