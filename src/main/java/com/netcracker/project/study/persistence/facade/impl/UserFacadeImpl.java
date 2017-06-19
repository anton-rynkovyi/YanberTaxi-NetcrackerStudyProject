package com.netcracker.project.study.persistence.facade.impl;

import com.netcracker.project.study.model.client.ClientAttr;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.facade.UserFacade;
import com.netcracker.project.study.persistence.manager.impl.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserFacadeImpl implements UserFacade{

    @Autowired
    PersistenceManager manager;

    public User createUser(User user) {
        User someUser = manager.createUser(user);
        return someUser;
    }

    public Long getObjectTypeIdByUser(User user) {
        Long userObjectTypeId = manager.getObjectTypeIdByUser(user);
        return userObjectTypeId;
    }

    public User findUserByUsername(String username) {
        User user = manager.findUserByUsername(username);
        if (user == null) {
            return null;
        }
        return user;
    }

    public User findClientByUsername(String phone){
        User user = findUserByUsername(phone);
        if (user == null) {
            return null;
        }
        Long objectTypeId = getObjectTypeIdByUser(user);
        if (objectTypeId != null && objectTypeId  == ClientAttr.OBJECT_TYPE_ID) {
            return user;
        }
        return null;
    }

    public User findDriverByUsername(String phone){
        User user = findUserByUsername(phone);
        if (getObjectTypeIdByUser(user) == DriverAttr.OBJECT_TYPE_ID) {
            return user;
        }
        return null;
    }
}
