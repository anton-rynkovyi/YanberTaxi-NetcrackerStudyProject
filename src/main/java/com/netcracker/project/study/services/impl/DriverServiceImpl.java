package com.netcracker.project.study.services.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.client.ClientAttr;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.driver.car.CarAttr;
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
        List<Driver> driverList = persistenceFacade.getSome(query, Driver.class);
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
                "AND attr1.attr_id = " + DriverAttr.UNBAN_DATE_ATTR + " " +
                "AND attr1.date_value IS NULL";

        List<Driver> driverList = persistenceFacade.getSome(query, Driver.class);
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

        List<Car> carList = persistenceFacade.getSome(query, Car.class);
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
        List<Driver> driverBanList = persistenceFacade.getSome(query, Driver.class);
        return driverBanList;
    }


}
