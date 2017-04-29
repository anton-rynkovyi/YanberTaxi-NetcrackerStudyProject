package com.netcracker.project.study.model.admin;

import com.netcracker.project.study.model.Model;

public class Admin extends Model {

    private String phoneNumber;
    private String email;

    public Admin(){}

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
