package com.netcracker.project.study.services.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.ClientAttr;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.model.driver.car.CarAttr;
import com.netcracker.project.study.model.order.OrderAttr;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.facade.UserFacade;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.AdminService;
import com.netcracker.project.study.services.DriverService;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversBanGrid;
import com.netcracker.project.study.vaadin.admin.components.grids.DriversGrid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Date;
import java.util.List;

@Service
@Scope(value = "prototype")
public class AdminServiceImpl implements AdminService {

    public final long ONE_DAY_MILLS = 1000 * 60 * 60 * 24;

    @Autowired
    PersistenceFacade persistenceFacade;

    @Autowired
    UserFacade userFacade;

    @Autowired
    DriverService driverService;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    HttpSessionEventPublisher httpSessionEventPublisher;


    @Override
    public boolean isVerificate(Driver driver) {
        return false;
    }

    @Override
    public void showTracking() {

    }

    @Override
    public void giveBan(Driver driver, int days) {
        Date date = new Date(System.currentTimeMillis() + ONE_DAY_MILLS * days); //todo "1000*60" change to ONE_DAY_MILLS const
        User user = userFacade.findDriverDetailsByUsername(driver.getPhoneNumber());
        driver.setUnbanDate(date);
        driver.setStatus(DriverStatusList.OFF_DUTY);
        persistenceFacade.update(driver);
        user.setEnabled(false);
        userFacade.updateUser(user);
    }


    private void logoutSession(String sessionId) {
        SessionInformation session = sessionRegistry.getSessionInformation(sessionId);
        if (session != null) {
            session.expireNow();
        }
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
               User user = userFacade.findDriverDetailsByUsername(driver.getPhoneNumber());
               user.setEnabled(true);
               userFacade.updateUser(user);
/*               driversGrid.refreshGrid();
               driversBanGrid.refreshGrid()*/;
           } else {
               //System.out.println(driver.getObjectId()+": " + dif / 1000);
           }
        }
    }




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

    @Override
    public void fireDriver(Driver driver) {
        driver.setStatus(DriverStatusList.DISMISSED);
        persistenceFacade.update(driver);
    }
}
