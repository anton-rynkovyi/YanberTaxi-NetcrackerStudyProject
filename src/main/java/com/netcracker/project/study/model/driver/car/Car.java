package com.netcracker.project.study.model.driver.car;

import com.netcracker.project.study.model.Model;

import java.util.Date;


public class Car extends Model implements CarAttr {

    public final int OBJECT_TYPE_ID = 4;

    private String makeOfCar;
    private String modelType;
    private Date releaseDate;
    private int seatsCount;
    private int driverId;
    private int stateNumber;
    private boolean childSeat;

    public Car(long objectId){
        this.objectId = objectId;
    }

    public String getModelType() {
        return modelType;
    }

    public String getMakeOfCar() {
        return makeOfCar;
    }

    public void setMakeOfCar(String makeOfCar) {
        this.makeOfCar = makeOfCar;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getSeatsCount() {
        return seatsCount;
    }

    public void setSeatsCount(int seatsCount) {
        this.seatsCount = seatsCount;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(int stateNumber) {
        this.stateNumber = stateNumber;
    }

    public boolean isChildSeat() {
        return childSeat;
    }

    public void setChildSeat(boolean childSeat) {
        this.childSeat = childSeat;
    }

}
