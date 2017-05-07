package com.netcracker.project.study.model.order.status;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;
import com.netcracker.project.study.model.annotations.Reference;

import java.sql.Time;

@ObjectType(objectTypeId = 5)
public class OrderStatus extends Model implements OrderStatusAttr {

    @Attribute(attrId = 30)
    @Reference(objectTypeId = 3)
    private int orderId;

    @Attribute(attrId = 31)
    private String status;

    @Attribute(attrId = 32)
    private Time timeStamp;

    public OrderStatus() {}

    public OrderStatus(long objectId) {
        super(objectId);
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
