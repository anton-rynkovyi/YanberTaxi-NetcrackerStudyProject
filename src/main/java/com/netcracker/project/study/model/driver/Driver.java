package com.netcracker.project.study.model.driver;


import java.util.Date;

/**
 * Created by Mark on 25.04.2017.
 */
public class Driver {

    public final int OBJECT_TYPE_ID=1;

    private String name;
    private String description;

    private String lastName;
    private String firstName;
    private String middleName;

    private int phoneNumber;
    private int rating;
    private int experience;

    private String email;
    private String status;

    private Date hireDate;
    private Date unbanDate;


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

    public int getRating(){
        return rating;
    }
    public void setRating(int rating){
        this.rating=rating;
    }
    public int getExperience(){
        return experience;
    }
    public void setExperience(int experience){
        this.experience=experience;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status=status;
    }

    public Date getHireDate(){
        return hireDate;
    }

    public void setHireDate(Date hireDate){
        this.hireDate=hireDate;
    }

    public Date getUnbanDate(){
        return unbanDate;
    }

    public void setUnbanDate(Date unbanDate){
        this.unbanDate=unbanDate;
    }


}
