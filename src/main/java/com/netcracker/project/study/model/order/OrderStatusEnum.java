package com.netcracker.project.study.model.order;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public enum  OrderStatusEnum {

    NEW(OrderStatusList.NEW, "New"),
    ACCEPTED(OrderStatusList.ACCEPTED, "Accepted"),
    PERFORMED(OrderStatusList.PERFORMED, "Performed"),
    PERFORMING(OrderStatusList.PERFORMING, "Performing"),
    CANCELED(OrderStatusList.CANCELED, "Canceled");

    private BigInteger statusId;
    private String statusValue;

    OrderStatusEnum(BigInteger statusId, String statusValue) {
        this.statusId = statusId;
        this.statusValue = statusValue;
    }

    static {
        List<OrderStatusEnum> statuses = new ArrayList();
        for (OrderStatusEnum status : values()) {
            statuses.add(status);
        }
    }

    public static String getStatusValue(BigInteger statusId){
        for (OrderStatusEnum driverStatusEnum : values()){
            if (driverStatusEnum.statusId == statusId) return driverStatusEnum.statusValue;
        }
        return null;
    }
}
