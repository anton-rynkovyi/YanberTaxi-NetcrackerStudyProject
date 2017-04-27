package com.netcracker.project.study.model.driver.driver_log;

import java.sql.Time;

/**
 * Created by Mark on 27.04.2017.
 */
public class DriverLog {
    public final int OBJECT_TYPE_ID=6;

    private int driverId;
    private String status;
    private Time timeStamp;

    public int getDriverId(){
        return driverId;
    }

    public void setDriverId(int driverId){
        this.driverId=driverId;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status=status;
    }

    public Time getTimeStamp(){
        return timeStamp;
    }

    public void setTimeStamp(Time timeStamp){
        this.timeStamp=timeStamp;
    }
}
