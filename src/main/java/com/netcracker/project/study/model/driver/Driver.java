package com.netcracker.project.study.model.driver;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;

import java.util.Date;

@ObjectType(objectTypeId = 1)
public class Driver extends Model implements DriverAttr{

    @Attribute(attrId = 1)
    private String lastName;

    @Attribute(attrId = 2)
    private String firstName;

    @Attribute(attrId = 3)
    private String middleName;

    @Attribute(attrId = 4)
    private String phoneNumber;

    @Attribute(attrId = 5)
    private int rating;

    @Attribute(attrId = 6)
    private int experience;

    @Attribute(attrId = 7)
    private String email;

    @Attribute(attrId = 8)
    private String status;

    @Attribute(attrId = 9)
    private Date hireDate;

    @Attribute(attrId = 10)
    private Date unbanDate;

    public Driver(long objectId){
        this.objectId=objectId;
    }

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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Date getUnbanDate() {
        return unbanDate;
    }

    public void setUnbanDate(Date unbanDate) {
        this.unbanDate = unbanDate;
    }

}
