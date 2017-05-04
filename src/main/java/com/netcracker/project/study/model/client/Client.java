package com.netcracker.project.study.model.client;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;

@ObjectType(objectTypeId = 2)
public class Client extends Model implements ClientAttr {

    @Attribute(attrId = 11)
    private String lastName;

    @Attribute(attrId = 12)
    private String firstName;

    @Attribute(attrId = 13)
    private String middleName;

    @Attribute(attrId = 14)
    private String phoneNumber;

    @Attribute(attrId = 15)
    private int points;

    public Client(long objectId){
        this.objectId = objectId;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
