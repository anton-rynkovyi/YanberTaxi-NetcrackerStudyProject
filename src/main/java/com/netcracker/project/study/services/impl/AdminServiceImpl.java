package com.netcracker.project.study.services.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.ClientAttr;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.driver.car.CarAttr;
import com.netcracker.project.study.model.driver.status.DriverStatusValues;
import com.netcracker.project.study.model.order.OrderAttr;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversBanGrid;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@Service
public class AdminServiceImpl implements AdminService{

    public final long ONE_DAY_MILLS = 1000 * 60 * 60 * 24;


    @Autowired
    PersistenceFacade persistenceFacade;

    @Autowired
    DriversGrid driversGrid;

    @Autowired
    DriversBanGrid driversBanGrid;

    @Override
    public boolean isVerificate(Driver driver) {
        return false;
    }

    @Override
    public void showTracking() {

    }

    @Override
    public void giveBan(Driver driver, int days) {
        Date date = new Date(System.currentTimeMillis() + 1000*60 * days);
        driver.setUnbanDate(date);
        persistenceFacade.update(driver);
        setBanTimer(driver);
    }

    @Override
    public void setBanTimer(Driver driver) {
        Timer timer = new Timer();

        class BanTimer extends TimerTask {

            @Override
            public void run() {
                long def = driver.getUnbanDate().getTime() - System.currentTimeMillis();
                Driver d = persistenceFacade.getOne(driver.getObjectId(), Driver.class);
                if (def < 0 || d.getUnbanDate() == null) {
                    driver.setUnbanDate(null);
                    persistenceFacade.update(driver);
                    System.out.println("FINISH");
                    driversGrid.refreshGrid();
                    driversBanGrid.refreshGrid();
                    timer.cancel();
                    timer.purge();
                    return;
                }else {
                    System.out.println(driver.getObjectId()+": " + def / 1000);
                }
            }
        }
        timer.schedule(new BanTimer(), 0, 1000);
    }


    @Override
    public <T extends Model> List<T> allModelsAsList(Class modelClass) {
        if (modelClass.getSimpleName().equals("Client")) {
            return persistenceFacade.getAll(BigInteger.valueOf(ClientAttr.OBJECT_TYPE_ID), modelClass);
        } else if (modelClass.getSimpleName().equals("Driver")) {
            return persistenceFacade.getAll(BigInteger.valueOf(Driver.OBJECT_TYPE_ID), modelClass);
        } else if (modelClass.getSimpleName().equals("Order")) {
            return persistenceFacade.getAll(BigInteger.valueOf(OrderAttr.OBJECT_TYPE_ID), modelClass);
        } else if (modelClass.getSimpleName().equals("Car")) {
            return persistenceFacade.getAll(BigInteger.valueOf(CarAttr.OBJECT_TYPE_ID), modelClass);
        }
        return null;
    }

    @Override
    public BigInteger deleteModel(Model model) {
        BigInteger objectId = model.getObjectId();
        persistenceFacade.delete(objectId);
        return objectId;
    }

    @Override
    public <T extends Model> T createModel(Model model) {
        persistenceFacade.create(model);
        return (T) model;
    }

    @Override
    public void updateModel(Model model) {
        persistenceFacade.update(model);
    }

    @Override
    public List<Driver> getDriversWithApproval() {
        String query = "" +
                "SELECT obj.object_id " +
                "FROM objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "AND obj.object_type_id = " + DriverAttr.OBJECT_TYPE_ID + " " +
                "AND attr.list_value_id = " + DriverStatusValues.APPROVAL;
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
                "AND attr.list_value_id <> " + DriverStatusValues.APPROVAL + " " +
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

    @Override
    public void unbanDriver(Driver driver) {
        driver.setUnbanDate(null);
        persistenceFacade.update(driver);
    }



}
