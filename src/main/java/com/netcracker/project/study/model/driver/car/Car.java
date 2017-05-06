package com.netcracker.project.study.model.driver.car;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;
import com.netcracker.project.study.model.annotations.Reference;

import java.util.Date;

@ObjectType(objectTypeId = 4)
public class Car extends Model {

    @Attribute(attrId = CarAttr.MAKE_OF_CAR_ATTR)
    private String makeOfCar;

    @Attribute(attrId = CarAttr.MODEL_TYPE_ATTR)
    private String modelType;

    @Attribute(attrId = CarAttr.RELEASE_DATE_ATTR)
    private Date releaseDate;

    @Attribute(attrId = CarAttr.SEATS_COUNT_ATTR)
    private int seatsCount;

    @Attribute(attrId = CarAttr.DRIVER_ID_ATTR)
    @Reference(objectTypeId = 1)
    private int driverId;

    @Attribute(attrId = CarAttr.STATE_NUMBER_ATTR)
    private int stateNumber;

    @Attribute(attrId = CarAttr.CHILD_SEAT)
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
