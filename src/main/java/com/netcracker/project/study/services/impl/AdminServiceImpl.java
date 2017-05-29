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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    PersistenceFacade persistenceFacade;

    @Override
    public boolean isVerificate(Driver driver) {
        return false;
    }

    @Override
    public void showTracking() {

    }

    @Override
    public void showDriverInfo(Driver driver) {

    }

    @Override
    public void giveBan(Driver driver) {

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
                "select obj.OBJECT_ID " +
                "from objects obj, Attributes attr " +
                "where obj.object_id = attr.object_id " +
                "and obj.object_type_id = " + DriverAttr.OBJECT_TYPE_ID + " " +
                "and attr.list_value_id = " + DriverStatusValues.APPROVAL;
        List<Driver> driverList = persistenceFacade.getSome(query, Driver.class);
        return driverList;
    }

    @Override
    public List<Driver> getDriversWithoutApproval() {
        String query = "" +
                "select obj.OBJECT_ID " +
                "from Objects obj, Attributes attr " +
                "where obj.object_id = attr.object_id " +
                "and obj.object_type_id = " + DriverAttr.OBJECT_TYPE_ID + " " +
                "and attr.list_value_id <> " + DriverStatusValues.APPROVAL;
        List<Driver> driverList = persistenceFacade.getSome(query, Driver.class);
        return driverList;
    }

    @Override
    public List<Car> getCarByDriver(Driver driver) {
        String query = "" +
                "select obj.object_id " +
                "from objects obj,objreference ref " +
                "where ref.reference = " + driver.getObjectId() + " " +
                "and ref.attr_id = " + CarAttr.DRIVER_ID_ATTR + " " +
                "and ref.object_id = obj.object_id";

        List<Car> carList = persistenceFacade.getSome(query, Car.class);
        return carList;
    }
}
