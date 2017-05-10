package com.netcracker.project.study.model.order.status;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;
import com.netcracker.project.study.model.annotations.Reference;
import com.netcracker.project.study.model.order.OrderAttr;

import java.sql.Time;
import java.sql.Timestamp;

@ObjectType(objectTypeId = OrderStatusAttr.OBJECT_TYPE_ID)
public class OrderStatus extends Model{

    @Reference(attrId = OrderStatusAttr.ORDER_ID_ATTR)
    private int orderId;

    @Attribute(attrId = OrderStatusAttr.STATUS_ATTR)
    private int status;

    @Attribute(attrId = OrderStatusAttr.TIME_STAMP_ATTR)
    private Timestamp timeStamp;

    public OrderStatus() {}

    public OrderStatus(String name) {
        super(name);
    }

    public OrderStatus(String name, String description) {
        super(name, description);
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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
