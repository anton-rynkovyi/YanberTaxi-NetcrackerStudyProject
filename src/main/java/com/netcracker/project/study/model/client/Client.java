package com.netcracker.project.study.model.client;


import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;

@ObjectType(objectTypeId = ClientAttr.OBJECT_TYPE_ID)
public class Client extends Model implements ClientAttr {

    @Attribute(attrId = ClientAttr.LAST_NAME_ATTR)
    private String lastName;

    @Attribute(attrId = ClientAttr.FIRST_NAME_ATTR)
    private String firstName;

    @Attribute(attrId = ClientAttr.MIDDLE_NAME_ATTR)
    private String middleName;

    @Attribute(attrId = ClientAttr.PHONE_NUMBER_ATTR)
    private String phoneNumber;

    @Attribute(attrId = ClientAttr.POINTS_ATTR)
    private int points;

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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
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
