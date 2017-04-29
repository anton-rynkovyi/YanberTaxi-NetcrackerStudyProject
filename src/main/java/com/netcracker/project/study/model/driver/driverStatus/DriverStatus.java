package com.netcracker.project.study.model.driver.driverStatus;

import com.netcracker.project.study.model.Model;

import java.sql.Time;

public class DriverStatus extends Model {

    public final int OBJECT_TYPE_ID = 6;

    private int driverId;
    private String status;
    private Time timeStamp;

    public DriverStatus(){}

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Time getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Time timeStamp) {
        this.timeStamp = timeStamp;
    }

}
