package com.netcracker.project.study.model.driver.status;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;
import com.netcracker.project.study.model.annotations.Reference;
import com.netcracker.project.study.model.driver.DriverAttr;

import java.sql.Time;
import java.sql.Timestamp;

@ObjectType(objectTypeId = DriverStatusAttr.OBJECT_TYPE_ID)
public class DriverStatus extends Model{


    @Reference(attrId = DriverStatusAttr.DRIVER_ID_ATTR)
    private int driverId;

    @Attribute(attrId = DriverStatusAttr.STATUS_ATTR)
    private int status;

    @Attribute(attrId = DriverStatusAttr.TIME_STAMP_ATTR)
    private Timestamp timeStamp;

    public DriverStatus() {}

    public DriverStatus(String name) {
        super(name);
    }

    public DriverStatus(String name, String description) {
        super(name, description);
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

}
