package com.netcracker.project.study.model.order.status;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;
import com.netcracker.project.study.model.annotations.Reference;
import com.netcracker.project.study.model.order.OrderAttr;

import java.sql.Time;

@ObjectType(objectTypeId = OrderStatusAttr.OBJECT_TYPE_ID)
public class OrderStatus extends Model{

    @Reference(objectTypeId = OrderAttr.OBJECT_TYPE_ID)
    private int orderId;

    @Attribute(attrId = OrderStatusAttr.STATUS_ATTR)
    private String status;

    @Attribute(attrId = OrderStatusAttr.TIME_STAMP_ATTR)
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
