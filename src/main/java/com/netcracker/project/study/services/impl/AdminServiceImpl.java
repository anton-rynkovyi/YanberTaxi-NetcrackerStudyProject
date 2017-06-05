package com.netcracker.project.study.services.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.ClientAttr;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.driver.car.Car;
import com.netcracker.project.study.model.driver.car.CarAttr;
import com.netcracker.project.study.model.order.Order;
import com.netcracker.project.study.model.order.OrderAttr;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversBanGrid;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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

    @Autowired
    DriverService driverService;

    @Override
    public boolean isVerificate(Driver driver) {
        return false;
    }

    @Override
    public void showTracking() {

    }

    @Override
    public void giveBan(Driver driver, int days) {
        Date date = new Date(System.currentTimeMillis() + 1000*60 * days); //todo "1000*60" change to ONE_DAY_MILLS const
        driver.setUnbanDate(date);
        driver.setStatus(DriverStatusList.OFF_DUTY);
        persistenceFacade.update(driver);
    }


    @Override
    public void setBanTask() {
        List<Driver> driverList = driverService.getBannedDrivers();
        for (int i = 0; i < driverList.size(); i++) {
            Driver driver = driverList.get(i);
           long dif = driver.getUnbanDate().getTime() - System.currentTimeMillis();
           if (dif < 0 || driver.getUnbanDate() == null) {
               driver.setUnbanDate(null);
               persistenceFacade.update(driver);
               System.out.println("FINISH");
               driversGrid.refreshGrid();
               driversBanGrid.refreshGrid();
           } else {
               System.out.println(driver.getObjectId()+": " + dif / 1000);
           }
        }
    }

   /* @Override
    public void setBanTimer(Driver driver) {

        //Timer timer = new Timer();

        *//*class BanTimer extends TimerTask {

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
        }*//*
       // timer.schedule(new BanTimer(), 0, 1000);
    }*/


    @Override
    public <T extends Model> T getModelById(BigInteger modelId, Class modelClass) {
        T model = persistenceFacade.getOne(modelId, modelClass);
        return model != null ? model : null;
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
    public void unbanDriver(Driver driver) {
        driver.setUnbanDate(null);
        persistenceFacade.update(driver);
    }
}
