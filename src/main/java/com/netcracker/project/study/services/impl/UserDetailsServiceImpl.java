package com.netcracker.project.study.services.impl;

import com.google.common.collect.ImmutableList;
import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.model.admin.Admin;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import com.netcracker.project.study.model.driver.DriverAttr;
import com.netcracker.project.study.persistence.facade.impl.PersistenceFacade;
import org.apache.xpath.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PersistenceFacade persistenceFacade;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        //Driver userDriver = persistenceFacade.getOne(BigInteger.valueOf(115), Driver.class);
        Driver user = new Driver();
        user.setUsername("admin");
        user.setPassword("123");
        user.setAuthorities(ImmutableList.of(Role.ROLE_DRIVER));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);

        return user;
    }

















    public <T extends Model> T findUserByPhoneNumber(String phoneNumber) {
        String query = "" +
                "SELECT obj.object_id " +
                "FROM Objects obj " +
                "INNER JOIN Attributes attr ON obj.object_id = attr.object_id " +
                "WHERE attr.value = '" + phoneNumber + "'";
        List<Model> users = persistenceFacade.getSome(query, Model.class);
        if (users != null && !users.isEmpty()) {
            Model user = users.get(0);
            if (user instanceof Driver) {
                return ((T) user);
            } else if (user instanceof Client) {
                return (T) user;
            } else if (user instanceof Admin) {
                return (T) user;
            }
        }
        return null;
    }
}
