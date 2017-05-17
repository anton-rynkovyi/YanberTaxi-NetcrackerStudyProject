package com.netcracker.project.study.model.driver.status;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.*;
import com.netcracker.project.study.model.driver.DriverAttr;

import java.io.InputStream;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

@ObjectType(objectTypeId = DriverStatusAttr.OBJECT_TYPE_ID)
public class DriverStatus extends Model{


    public static final BigInteger APPROVAL = BigInteger.valueOf(16);

    public static final BigInteger OFF_DUTY = BigInteger.valueOf(17);

    public static final BigInteger FREE = BigInteger.valueOf(18);

    public static final BigInteger ON_CALL = BigInteger.valueOf(19);

    public static final BigInteger PERFORMING_ORDER = BigInteger.valueOf(20);


    @Reference(attrId = DriverStatusAttr.DRIVER_ID_ATTR)
    private BigInteger driverId;

    @Attribute(attrId = DriverStatusAttr.STATUS_ATTR)
    private @AttrList BigInteger status;

    @Attribute(attrId = DriverStatusAttr.TIME_STAMP_ATTR)
    private @AttrDate Timestamp timeStamp;

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

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

}
