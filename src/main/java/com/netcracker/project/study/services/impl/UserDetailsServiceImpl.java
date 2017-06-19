package com.netcracker.project.study.services.impl;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.client.ClientAttr;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.user.User;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.persistence.facade.impl.UserFacadeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PersistenceFacade persistenceFacade;

    @Autowired
    UserFacadeImpl userFacadeImpl;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userFacadeImpl.findUserByUsername(phoneNumber);
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
        long objectTypeId = userFacadeImpl.getObjectTypeIdByUser(user);
        if (objectTypeId == ClientAttr.OBJECT_TYPE_ID) {
            Client client = persistenceFacade.getOne(user.getObjectId(), Client.class);
            return (T) client;
        } else if (objectTypeId == DriverAttr.OBJECT_TYPE_ID) {
            Driver driver = persistenceFacade.getOne(user.getObjectId(), Driver.class);
            return (T) driver;
        }
        return null;
    }

    public boolean isClientLoginExist(String phone) {
        User user = userFacadeImpl.findClientByUsername(phone);
        if (user == null) {
            return false;
        }
        if (user.getUsername().equals(phone)) {
            return true;
        }
        return false;
    }
}
