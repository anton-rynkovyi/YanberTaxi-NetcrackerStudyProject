package com.netcracker.project.study.model.driver.status;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.*;

import java.math.BigInteger;
import java.util.Date;

@ObjectType(objectTypeId = DriverStatusAttr.OBJECT_TYPE_ID)
public class DriverStatus extends Model implements DriverStatusAttr{

    @Reference(attrId = DriverStatusAttr.DRIVER_ID_ATTR)
    private BigInteger driverId;

    @Attribute(attrId = DriverStatusAttr.STATUS_ATTR)
    private @AttrList BigInteger status;

    @Attribute(attrId = DriverStatusAttr.TIME_STAMP_ATTR)
    private @AttrDate Date timeStamp;

    public DriverStatus() {}

    public DriverStatus(String name) {
        super(name);
    }

    public DriverStatus(String name, String description) {
        super(name, description);
    }

    public BigInteger getDriverId() {
        return driverId;
    }

    public void setDriverId(BigInteger driverId) {
        this.driverId = driverId;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

}
