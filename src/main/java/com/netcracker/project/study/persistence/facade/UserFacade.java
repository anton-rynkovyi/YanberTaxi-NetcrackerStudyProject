package com.netcracker.project.study.persistence.facade;


import com.netcracker.project.study.model.user.User;

public interface UserFacade {

    User createUser(User user);
    Long getObjectTypeIdByUser(User user);
    User findUserByUsername(String username);
    User findClientByUsername(String phone);
    User findDriverByUsername(String phone);
}
