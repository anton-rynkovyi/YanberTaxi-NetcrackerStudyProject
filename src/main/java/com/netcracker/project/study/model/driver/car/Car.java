package com.netcracker.project.study.model.driver.car;

import javax.xml.soap.SAAJResult;
import java.util.Date;

/**
 * Created by Mark on 25.04.2017.
 */
public class Car {
    public final int OBJECT_TYPE_ID=4;

   private String name;
   private String modelType;
   private Date releaseDate;
   private int seatsCount;
   private int driverId;
   private int stateNumber;
   private boolean childSeat;

   public String getName(){
       return name;
   }

   public void setName(String name){
       this.name=name;
   }

   public String getModelType(){
       return modelType;
   }

   public void setModelType(String modelType){
       this.modelType=modelType;
   }

   public Date getReleaseDate(){
       return releaseDate;
   }

   public void setReleaseDate(Date releaseDate){
       this.releaseDate=releaseDate;
   }

   public int getSeatsCount(){
       return seatsCount;
   }

   public void setSeatsCount(int seatsCount){
       this.seatsCount=seatsCount;
   }

   public int getDriverId(){
       return driverId;
   }

   public void setDriverId(int driverId){
       this.driverId=driverId;
   }

   public int getStateNumber(){
       return stateNumber;
   }

   public void setStateNumber(int stateNumber){
       this.stateNumber=stateNumber;
   }

   public boolean getChildSeat(){
       return childSeat;
   }

   public void setChildSeat(boolean childSeat){
       this.childSeat=childSeat;
   }
}
