package com.netcracker.project.study.services;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.order.Order;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

public interface AdminService {

    boolean isVerificate(Driver driver);

    void showTracking();

    void giveBan(Driver driver, int days);

    <T extends Model> List<T> allModelsAsList(Class modelClass);

    BigInteger deleteModel(Model model);

    <T extends Model> T createModel(Model model);

    void updateModel(Model model);

    List<Driver> getActiveDrivers();

    List<Car> getCarByDriver(Driver driver);

    List<Driver> getBannedDrivers();

    void unbanDriver(Driver driver);

    //void setBanTimer(Driver driver);

    void setBanTask();

    List<Driver> getDriversByStatusId(BigInteger statusId);

    List<Order> getOrdersByDriverId(BigInteger driverId);

    List<Order> getOrdersByClientId(BigInteger clientId);

    <T extends Model> T getModelById(BigInteger modelId, Class modelClass);
}
