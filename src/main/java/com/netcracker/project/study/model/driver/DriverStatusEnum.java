package com.netcracker.project.study.model.driver;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public enum DriverStatusEnum {
    APPROVAL(DriverStatusList.APPROVAL, "Approval"),
    OFF_DUTY(DriverStatusList.OFF_DUTY, "Off duty"),
    FREE(DriverStatusList.FREE, "Free"),
    ON_CALL(DriverStatusList.ON_CALL, "On call"),
    PERFORMING_ORDER(DriverStatusList.PERFORMING_ORDER, "Performing order");

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
