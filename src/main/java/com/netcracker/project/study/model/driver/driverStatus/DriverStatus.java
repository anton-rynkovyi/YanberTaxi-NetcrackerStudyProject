package com.netcracker.project.study.model.driver.driverStatus;

import java.sql.Time;

public class DriverStatus {

    public final int OBJECT_TYPE_ID = 6;

    private int driverId;
    private String status;
    private Time timeStamp;

    private String name;
    private String description;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
