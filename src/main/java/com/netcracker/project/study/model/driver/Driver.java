package com.netcracker.project.study.model.driver;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;

import java.util.Date;

@ObjectType(objectTypeId = 1)
public class Driver extends Model{

    @Attribute(attrId = DriverAttr.LAST_NAME_ATTR)
    private String lastName;

    @Attribute(attrId = DriverAttr.FIRST_NAME_ATTR)
    private String firstName;

    @Attribute(attrId = DriverAttr.MIDDLE_NAME_ATTR)
    private String middleName;

    @Attribute(attrId = DriverAttr.PHONE_NUMBER_ATTR)
    private String phoneNumber;

    @Attribute(attrId = DriverAttr.RATING_ATTR)
    private int rating;

    @Attribute(attrId = DriverAttr.EXPERIENCE_ATTR)
    private int experience;

    @Attribute(attrId = DriverAttr.EMAIL_ATTR)
    private String email;

    @Attribute(attrId = DriverAttr.STATUS_ATTR)
    private String status;

    @Attribute(attrId = DriverAttr.HIRE_DATE_ATTR)
    private Date hireDate;

    @Attribute(attrId = DriverAttr.UNBAN_DATE_ATTR)
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
