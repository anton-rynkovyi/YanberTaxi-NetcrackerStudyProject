package com.netcracker.project.study.model.client;


import com.netcracker.project.study.model.Model;

public class Client  extends Model {

    private final int OBJECT_TYPE_ID = 2;

    private String lastName;
    private String firstName;
    private String middleName;

    private String phoneNumber;
    private int points;

    public Client(){}

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
