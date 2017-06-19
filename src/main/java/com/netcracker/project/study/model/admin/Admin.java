package com.netcracker.project.study.model.admin;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.Role;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public class Admin extends Model implements AdminAttr {

    private String phoneNumber;

    private String email;


    public Admin(){}

    public Admin(String name) {
        super(name);
    }

    public Admin(String name, String description) {
        super(name, description);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
