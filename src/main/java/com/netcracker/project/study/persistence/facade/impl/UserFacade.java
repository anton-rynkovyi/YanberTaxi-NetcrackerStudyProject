package com.netcracker.project.study.persistence.facade.impl;

import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.manager.impl.PersistenceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserFacade {

    @Autowired
    PersistenceManager manager;

    public User createUser(User user) {
        User someUser = manager.createUser(user);
        return someUser;
    }

    public long getObjectTypeIdByUser(User user) {
        long userObjectTypeId = manager.getObjectTypeIdByUser(user);
        return userObjectTypeId;
    }

    public User findUserByUsername(String username) {
        User user = manager.findUserByUsername(username);
        return user;
    }
}
