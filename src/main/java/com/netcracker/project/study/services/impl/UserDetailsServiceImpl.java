package com.netcracker.project.study.services.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.admin.Admin;
import com.netcracker.project.study.model.admin.AdminAttr;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.client.ClientAttr;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.facade.UserFacade;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.services.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PersistenceFacade persistenceFacade;

    @Autowired
    UserFacade userFacade;

    @Autowired
    DriverService driverService;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userFacade.findUserDetailsByUsername(phoneNumber);
        return user;
    }

    public boolean hasRole(String role) {
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        boolean hasRole = false;
        for (GrantedAuthority authority : authorities) {
            hasRole = authority.getAuthority().equals(role);
            if (hasRole) {
                break;
            }
        }
        return hasRole;
    }


    public <T extends Model> T getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long objectTypeId = userFacade.getObjectTypeIdByUser(user);
        if (objectTypeId == ClientAttr.OBJECT_TYPE_ID) {
            Client client = persistenceFacade.getOne(user.getObjectId(), Client.class);
            return (T) client;
        }
        if (objectTypeId == DriverAttr.OBJECT_TYPE_ID) {
            Driver driver = persistenceFacade.getOne(user.getObjectId(), Driver.class);
            return (T) driver;
        }
        if (objectTypeId == AdminAttr.OBJECT_TYPE_ID) {
            Admin admin = persistenceFacade.getOne(user.getObjectId(), Admin.class);
            return (T) admin;
        }
        return null;
    }

    public <User> User getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    public boolean isClientLoginExist(String phone) {
        User user = userFacade.findClientDetailsByUsername(phone);
        if (user == null) {
            return false;
        }
        if (user.getUsername().equals(phone)) {
            return true;
        }
        return false;
    }

    public boolean isDriverLoginExist(String phone) {
        User user = userFacade.findDriverDetailsByUsername(phone);
        if (user == null) {
            return false;
        }
        if (user.getUsername().equals(phone)) {
            return true;
        }
        return false;
    }

    public boolean isLoginExist(String phone) {
        User user = userFacade.findUserDetailsByUsername(phone);
        if (user == null) {
            return false;
        }
        if (user.getUsername().equals(phone)) {
            return true;
        }
        return false;
    }

    public boolean isEmailExist(String email) {
       if (isDriverEmailExist(email) || isAdminEmailExist(email)) {
           return true;
       }
       return false;
    }

    private boolean isDriverEmailExist(String email){
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE attr.attr_id = " + DriverAttr.EMAIL_ATTR + " " +
                "AND attr.value = '" + email + "'";
        List<Driver> drivers = persistenceFacade.getSome(query, Driver.class,false);
        System.out.println("Driver: " + drivers);
        if (drivers.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isAdminEmailExist(String email){
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE attr.attr_id = " + AdminAttr.EMAIL_ATTR + " " +
                "AND attr.value = '" + email + "'";
        List<Admin> admins = persistenceFacade.getSome(query, Admin.class,false);
        if (admins.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public Driver findDriverByUserName(String phone) {
        User user = userFacade.findDriverDetailsByUsername(phone);
        Driver driver = persistenceFacade.getOne(user.getObjectId(), Driver.class);
        return driver;
    }

    public User findUserByUsername(String phone){
        User user = userFacade.findDriverDetailsByUsername(phone);
        return user;
    }

    public boolean isDriverBanned(Driver driver) {
        User user = userFacade.findDriverDetailsByUsername(driver.getPhoneNumber());
        if (user.isEnabled()) {
            return false;
        }
        return true;
    }

    public void deleteUser(User user) {
        userFacade.deleteUser(user);
    }
}
