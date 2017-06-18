package com.netcracker.project.study.model.user;



import com.netcracker.project.study.model.Role;
import com.netcracker.project.study.model.admin.Admin;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.util.List;

public class User implements UserDetails{

    private BigInteger userId;

    private BigInteger objectId;

    private List<Role> authorities;

    private String username;

    private String password;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;

    private Driver userDriver;

    private Client userClient;

    private Admin userAdmin;

    public User() {}


    public User(String phone, String password) {
        this.username = phone;
        this.password = password;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigInteger getObjectId() {
        return objectId;
    }

    public void setObjectId(BigInteger objectId) {
        this.objectId = objectId;
    }

    public void setAuthorities(List<Role> authorities) {
        this.authorities = authorities;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String phone) {
        this.username = phone;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    @Override
    public List<Role> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Driver getUserDriver() {
        return userDriver;
    }

    public void setUserDriver(Driver userDriver) {
        this.userDriver = userDriver;
    }

    public Client getUserClient() {
        return userClient;
    }

    public void setUserClient(Client userClient) {
        this.userClient = userClient;
    }

    public Admin getUserAdmin() {
        return userAdmin;
    }

    public void setUserAdmin(Admin userAdmin) {
        this.userAdmin = userAdmin;
    }
}
