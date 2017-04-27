package com.netcracker.project.study.model.driver;

import java.util.Date;

/**
 * Created by Mark on 25.04.2017.
 */
public class Driver {

    private String last_name;
    private String first_name;
    private String middle_name;

    private int phoneNumber;
    private int rating;
    private int experience;

    private String email;
    private String status;

    private Date hireDate;
    private Date unbanDate;

    public String getLast_name(){
        return last_name;
    }

    public void setLast_name(String last_name){
        this.last_name=last_name;
    }

    public String getFirst_name(){
        return first_name;
    }

    public void setFirst_name(String first_name){
        this.first_name=first_name;
    }

    public String getMiddle_name(){
        return middle_name;
    }

    public void setMiddle_name(String middle_name){
        this.middle_name=middle_name;
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
