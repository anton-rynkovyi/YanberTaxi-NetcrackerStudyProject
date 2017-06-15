package com.netcracker.project.study.services.impl;

import com.google.common.collect.ImmutableList;
import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.model.admin.Admin;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.model.driver.DriverStatusList;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import com.netcracker.project.study.vaadin.driver.components.views.OrdersViewForDrivers;
import com.vaadin.spring.annotation.ViewScope;
import org.apache.xpath.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PersistenceFacade persistenceFacade;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        /*Client user = persistenceFacade.getOne(BigInteger.valueOf(154), Client.class);
        //Driver user = new Driver();
        //user.setObjectId(BigInteger.valueOf(202));
        user.setUsername("client");
        user.setPassword("123");
        user.setAuthorities(ImmutableList.of(Role.ROLE_CLIENT));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
       //ordersViewForDrivers.init(user);*/

        Driver user = persistenceFacade.getOne(BigInteger.valueOf(202), Driver.class);
        //Client user = new Client();
        //user.setObjectId(BigInteger.valueOf(202));
        user.setUsername("driver");
        user.setPassword("123");
        user.setAuthorities(ImmutableList.of(Role.ROLE_DRIVER));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

       /* Admin user = new Admin();
        //user.setObjectId(BigInteger.valueOf(202));
        user.setUsername("admin");
        user.setPassword("123");
        user.setAuthorities(ImmutableList.of(Role.ROLE_ADMIN));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);*/

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














    public <T extends Model> T findUserByPhoneNumber(String phoneNumber) {
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE attr.value = '" + phoneNumber + "'";
        List<Model> users = persistenceFacade.getSome(query, Model.class);
        if (users != null && !users.isEmpty()) {

        }
        return null;
    }
}
