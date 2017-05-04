package com.netcracker.project.study.model.order.status;

import com.netcracker.project.study.model.Model;

import java.sql.Time;

public class OrderStatus extends Model implements OrderStatusAttr {

    private int orderId;
    private String status;
    private Time timeStamp;

    public OrderStatus(long objectId){
        this.objectId = objectId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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
