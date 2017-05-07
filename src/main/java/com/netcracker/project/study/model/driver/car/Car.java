package com.netcracker.project.study.model.driver.car;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;
import com.netcracker.project.study.model.annotations.Reference;

import java.util.Date;

@ObjectType(objectTypeId = 4)
public class Car extends Model implements CarAttr {

    @Attribute(attrId = 23)
    private String makeOfCar;

    @Attribute(attrId = 24)
    private String modelType;

    @Attribute(attrId = 25)
    private Date releaseDate;

    @Attribute(attrId = 26)
    private int seatsCount;

    @Attribute(attrId = 27)
    @Reference(objectTypeId = 1)
    private int driverId;

    @Attribute(attrId = 28)
    private int stateNumber;

    @Attribute(attrId = 29)
    private boolean childSeat;

    public Car() {}

    public Car(long objectId){
        super(objectId);
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
