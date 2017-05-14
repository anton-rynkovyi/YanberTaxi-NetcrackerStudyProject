package com.netcracker.project.study.model.driver;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.*;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;


@ObjectType(objectTypeId = DriverAttr.OBJECT_TYPE_ID)
public class Driver extends Model implements DriverAttr{


    public static final BigInteger APPROVAL = BigInteger.valueOf(1);

    public static final BigInteger OFF_DUTY = BigInteger.valueOf(2);

    public static final BigInteger FREE = BigInteger.valueOf(3);

    public static final BigInteger ON_CALL = BigInteger.valueOf(4);

    public static final BigInteger PERFORMING_ORDER = BigInteger.valueOf(5);


    @Attribute(attrId = DriverAttr.LAST_NAME_ATTR)
    private @AttrValue String lastName;

    @Attribute(attrId = DriverAttr.FIRST_NAME_ATTR)
    private @AttrValue String firstName;

    @Attribute(attrId = DriverAttr.MIDDLE_NAME_ATTR)
    private @AttrValue String middleName;

    @Attribute(attrId = DriverAttr.PHONE_NUMBER_ATTR)
    private @AttrValue String phoneNumber;

    @Attribute(attrId = DriverAttr.RATING_ATTR)
    private @AttrValue BigInteger rating;

    @Attribute(attrId = DriverAttr.EXPERIENCE_ATTR)
    private @AttrValue BigInteger experience;

    @Attribute(attrId = DriverAttr.EMAIL_ATTR)
    private @AttrValue String email;

    @Attribute(attrId = DriverAttr.STATUS_ATTR)
    private @AttrList BigInteger status;

    @Attribute(attrId = DriverAttr.HIRE_DATE_ATTR)
    private @AttrDate Timestamp hireDate;

    @Attribute(attrId = DriverAttr.UNBAN_DATE_ATTR)
    private @AttrDate Timestamp unbanDate;

    public Driver() {}

    public Driver(String name) {
        super(name);
    }

    public Driver(String name, String description) {
        super(name, description);
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

    public BigInteger getRating() {
        return rating;
    }

    public void setRating(BigInteger rating) {
        this.rating = rating;
    }

    public BigInteger getExperience() {
        return experience;
    }

    public void setExperience(BigInteger experience) {
        this.experience = experience;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    public Timestamp getHireDate() {
        return hireDate;
    }

    public void setHireDate(Timestamp hireDate) {
        this.hireDate = hireDate;
    }

    public Timestamp getUnbanDate() {
        return unbanDate;
    }

    public void setUnbanDate(Timestamp unbanDate) {
        this.unbanDate = unbanDate;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", rating=" + rating +
                ", experience=" + experience +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                ", hireDate=" + hireDate +
                ", unbanDate=" + unbanDate +
                '}';
    }
}
