package com.netcracker.project.study.model.client;


/**
 * Created by Mark on 25.04.2017.
 */
public class Client  {
    private final int OBJECT_TYPE_ID=2;

    private String name;
    private String description;

    private String lastName;
    private String firstName;
    private String middleName;

    private int phoneNumber;
    private int points;


    public int getObjectId() {
        return OBJECT_TYPE_ID;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name=name;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description=description;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getMiddleName(){
        return middleName;
    }

    public void setMiddleName(String middleName){
        this.middleName = middleName;
    }


    public int getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPoints(){
        return points;
    }

    public void setPoints(int points){
        this.points=points;
    }



}
