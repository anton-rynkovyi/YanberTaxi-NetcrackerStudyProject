package com.netcracker.project.study.model.driver;

import com.netcracker.project.study.model.driver.status.DriverStatus;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public enum DriverStatusEnum {
    APPROVAL(DriverStatusValues.APPROVAL, "Approval"),
    OFF_DUTY(DriverStatusValues.OFF_DUTY, "Off duty"),
    FREE(DriverStatusValues.FREE, "Free"),
    ON_CALL(DriverStatusValues.ON_CALL, "On call"),
    PERFORMING_ORDER(DriverStatusValues.PERFORMING_ORDER, "Performing order");

    private BigInteger statusId;
    private String statusValue;

    DriverStatusEnum(BigInteger statusId, String statusValue) {
        this.statusId = statusId;
        this.statusValue = statusValue;
    }

    static {
        List<DriverStatusEnum> statuses = new ArrayList();
        for (DriverStatusEnum status : values()) {
            statuses.add(status);
        }
    }

    public static String getStatusValue(BigInteger statusId){
        for (DriverStatusEnum driverStatusEnum : values()){
            if (driverStatusEnum.statusId == statusId) return driverStatusEnum.statusValue;
        }
        return null;
    }
}
