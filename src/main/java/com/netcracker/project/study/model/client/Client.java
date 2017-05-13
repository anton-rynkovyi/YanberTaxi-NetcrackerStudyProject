package com.netcracker.project.study.model.client;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.AttrValue;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;

import java.math.BigInteger;

@ObjectType(objectTypeId = ClientAttr.OBJECT_TYPE_ID)
public class Client extends Model implements ClientAttr {

    @Attribute(attrId = ClientAttr.LAST_NAME_ATTR)
    private @AttrValue String lastName;

    @Attribute(attrId = ClientAttr.FIRST_NAME_ATTR)
    private @AttrValue String firstName;

    @Attribute(attrId = ClientAttr.MIDDLE_NAME_ATTR)
    private @AttrValue String middleName;

    @Attribute(attrId = ClientAttr.PHONE_NUMBER_ATTR)
    private @AttrValue String phoneNumber;

    @Attribute(attrId = ClientAttr.POINTS_ATTR)
    private @AttrValue BigInteger points;

    public Client() {}

    public Client(String name) {
        super(name);
    }

    public Client(String name, String description) {
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

    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone;
    }

    public BigInteger getPoints() {
        return points;
    }

    public void setPoints(BigInteger points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Client{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", points=" + points +
                '}';
    }
}
