package com.netcracker.project.study.model.driver.car;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.*;

import java.math.BigInteger;
import java.util.Date;

@ObjectType(objectTypeId = CarAttr.OBJECT_TYPE_ID)
public class Car extends Model {

    @Attribute(attrId = CarAttr.MAKE_OF_CAR_ATTR)
    private @AttrValue String makeOfCar;

    @Attribute(attrId = CarAttr.MODEL_TYPE_ATTR)
    private @AttrValue String modelType;

    @Attribute(attrId = CarAttr.RELEASE_DATE_ATTR)
    private @AttrDate Date releaseDate;

    @Attribute(attrId = CarAttr.SEATS_COUNT_ATTR)
    private @AttrValue BigInteger seatsCount;

    @Reference(attrId = CarAttr.DRIVER_ID_ATTR)
    private BigInteger driverId;

    @Attribute(attrId = CarAttr.STATE_NUMBER_ATTR)
    private @AttrValue String stateNumber;

    @Attribute(attrId = CarAttr.CHILD_SEAT)
    private @AttrValue boolean childSeat;

    public Car() {}

    public Car(String name) {
        super(name);
    }

    public Car(String name, String description) {
        super(name, description);
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

    public BigInteger getSeatsCount() {
        return seatsCount;
    }

    public void setSeatsCount(BigInteger seatsCount) {
        this.seatsCount = seatsCount;
    }

    public BigInteger getDriverId() {
        return driverId;
    }

    public void setDriverId(BigInteger driverId) {
        this.driverId = driverId;
    }

    public String getStateNumber() {
        return stateNumber;
    }

    public void setStateNumber(String stateNumber) {
        this.stateNumber = stateNumber;
    }

    public boolean isChildSeat() {
        return childSeat;
    }

    public void setChildSeat(boolean childSeat) {
        this.childSeat = childSeat;
    }


    @Override
    public String toString() {
        return "Car{" +
                "makeOfCar='" + makeOfCar + '\'' +
                ", modelType='" + modelType + '\'' +
                ", releaseDate=" + releaseDate +
                ", seatsCount=" + seatsCount +
                ", driverId=" + driverId +
                ", stateNumber='" + stateNumber + '\'' +
                ", childSeat=" + childSeat +
                '}';
    }
}
