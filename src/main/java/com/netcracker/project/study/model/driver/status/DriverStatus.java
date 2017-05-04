package com.netcracker.project.study.model.driver.status;

import com.netcracker.project.study.model.Model;

import java.sql.Time;

public class DriverStatus extends Model implements DriverStatusAttr {

    private int driverId;
    private String status;
    private Time timeStamp;

    public DriverStatus(long objectId){
        this.objectId = objectId;
    }

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
