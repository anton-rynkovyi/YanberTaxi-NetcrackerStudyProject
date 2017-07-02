package com.netcracker.project.study.services.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.driver.car.CarAttr;
import com.netcracker.project.study.model.driver.status.DriverStatus;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.status.OrderStatus;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    @Autowired
    PersistenceFacade persistenceFacade;

    @Override
    public void acceptOrder(BigInteger orderId, BigInteger driverId) {
        Order order = persistenceFacade.getOne(orderId,Order.class);
        Driver driver = persistenceFacade.getOne(driverId,Driver.class);
        if(order.getStatus().equals(OrderStatus.NEW)){
            order.setDriverId(driverId);
            order.setStatus(OrderStatus.ACCEPTED);
            driver.setStatus(DriverStatusList.ON_CALL);
            persistenceFacade.update(order);
            persistenceFacade.update(driver);
        }else{
            throw new RuntimeException("The order must have status equals to new");
        }
    }

    @Override
    public <T extends Model> List<T> allModelsAsList() {
        List<T> clients = persistenceFacade.getAll(BigInteger.valueOf(DriverAttr.OBJECT_TYPE_ID), Driver.class);
        return clients;
    }

    @Override
    public List<Driver> getDriversByStatusId(BigInteger statusId) {
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "INNER JOIN Attributes attr1 ON obj.object_id = attr1.object_id " +
                "WHERE obj.object_type_id = " + DriverAttr.OBJECT_TYPE_ID + " " +
                "AND attr.attr_id = " + DriverAttr.STATUS_ATTR + " " +
                "AND attr.list_value_id = " + statusId + " " +
                "AND attr1.attr_id = " + DriverAttr.UNBAN_DATE_ATTR + " " +
                "AND attr1.date_value IS NULL";
        List<Driver> driverList = persistenceFacade.getSome(query, Driver.class,true);
        return driverList;
    }


    @Override
    public List<Driver> getActiveDrivers() {
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "INNER JOIN Attributes attr1 ON obj.object_id = attr1.object_id " +
                "WHERE obj.object_type_id = " + DriverAttr.OBJECT_TYPE_ID + " " +
                "AND attr.attr_id = " + DriverAttr.STATUS_ATTR + " " +
                "AND attr.list_value_id <> " + DriverStatusList.APPROVAL + " " +
                "AND attr.list_value_id <> " + DriverStatusList.DISMISSED + " " +
                "AND attr1.attr_id = " + DriverAttr.UNBAN_DATE_ATTR + " " +
                "AND attr1.date_value IS NULL";

        List<Driver> driverList = persistenceFacade.getSome(query, Driver.class,true);
        return driverList;
    }

    @Override
    public List<Car> getCarByDriver(Driver driver) {
        String query = "" +
                "SELECT obj.object_id " +
                "FROM objects obj " +
                "INNER JOIN objreference ref ON ref.object_id = obj.object_id " +
                "WHERE ref.reference = " + driver.getObjectId() + " " +
                "AND ref.attr_id = " + CarAttr.DRIVER_ID_ATTR;

        List<Car> carList = persistenceFacade.getSome(query, Car.class,false);
        return carList;
    }

    @Override
    public List<Driver> getBannedDrivers() {
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE obj.object_type_id = " + DriverAttr.OBJECT_TYPE_ID + " " +
                "AND attr.attr_id = " + DriverAttr.UNBAN_DATE_ATTR + " " +
                "AND attr.date_value IS NOT NULL";
        List<Driver> driverBanList = persistenceFacade.getSome(query, Driver.class,true);
        return driverBanList;
    }

    @Override
    @Transactional
    public void changeStatus(BigInteger status, Driver driver) {
        driver.setStatus(status);
        persistenceFacade.update(driver);
        driverStatusLog(driver);
    }

    @Override
    public void driverStatusLog(Driver driver) {
        DriverStatus driverStatus = new DriverStatus();
        driverStatus.setDriverId(driver.getObjectId());
        driverStatus.setStatus(driver.getStatus());
        driverStatus.setTimeStamp(new Date(System.currentTimeMillis()));
        persistenceFacade.create(driverStatus);
    }

    public void changeStatus(BigInteger status, BigInteger driverId) {
        Driver driver = persistenceFacade.getOne(driverId,Driver.class);
        driver.setStatus(status);
        persistenceFacade.update(driver);
    }

    public boolean isBanned(BigInteger driverId){
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE obj.object_type_id = " + DriverAttr.OBJECT_TYPE_ID + " " +
                "AND obj.object_id = " + driverId +
                " AND attr.attr_id = " + DriverAttr.UNBAN_DATE_ATTR + " " +
                "AND attr.date_value IS NOT NULL";

        List<Driver> driverBanList = persistenceFacade.getSome(query, Driver.class,true);
        if(driverBanList.isEmpty()){
            return false;
        }
        return true;
    }
}
