package com.netcracker.project.study.model.order.status;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.*;
import com.netcracker.project.study.model.order.OrderAttr;

import java.math.BigInteger;
import java.util.Date;

@ObjectType(objectTypeId = OrderStatusAttr.OBJECT_TYPE_ID)
public class OrderStatus extends Model{

    public static final BigInteger NEW = BigInteger.valueOf(6);

    public static final BigInteger ACCEPTED = BigInteger.valueOf(7);

    public static final BigInteger PERFORMED = BigInteger.valueOf(8);

    public static final BigInteger PERFORMING = BigInteger.valueOf(9);

    public static final BigInteger CANCELED = BigInteger.valueOf(10);
    @Reference(attrId = OrderStatusAttr.ORDER_ID_ATTR)
    private BigInteger orderId;

    @Attribute(attrId = OrderStatusAttr.STATUS_ATTR)
    private @AttrList BigInteger status;

    @Attribute(attrId = OrderStatusAttr.TIME_STAMP_ATTR)
    private @AttrDate Date timeStamp;

    public OrderStatus() {}

    public OrderStatus(String name) {
        super(name);
    }

    public OrderStatus(String name, String description) {
        super(name, description);
    }

    public BigInteger getOrderId() {
        return orderId;
    }

    public void setOrderId(BigInteger orderId) {
        this.orderId = orderId;
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
