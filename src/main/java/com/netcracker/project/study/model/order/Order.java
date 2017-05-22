package com.netcracker.project.study.model.order;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.*;

import java.math.BigDecimal;
import java.math.BigInteger;

@ObjectType(objectTypeId = OrderAttr.OBJECT_TYPE_ID)
public class Order extends Model implements OrderAttr{

    public static final BigInteger NEW = BigInteger.valueOf(6);

    public static final BigInteger ACCEPTED = BigInteger.valueOf(7);

    public static final BigInteger PERFORMED = BigInteger.valueOf(8);

    public static final BigInteger PERFORMING = BigInteger.valueOf(9);

    public static final BigInteger CANCELED = BigInteger.valueOf(10);

    @Reference(attrId = OrderAttr.DRIVER_ID_ATTR)
    private BigInteger driverId;

    @Reference(attrId = OrderAttr.CLIENT_ID_ATTR)
    private BigInteger clientId;

    @Attribute(attrId = OrderAttr.STATUS_ATTR)
    private @AttrList BigInteger status;

    @Attribute(attrId = OrderAttr.COST_ATTR)
    private @AttrValue BigDecimal cost;

    @Attribute(attrId = OrderAttr.DISTANCE_ATTR)
    private @AttrValue BigInteger distance;

    @Attribute(attrId = OrderAttr.DRIVER_RATING_ATTR)
    private BigInteger driverRating;

    @Attribute(attrId = OrderAttr.DRIVER_MEMO_ATTR)
    private @AttrValue String driverMemo;

    public Order() {}

    public Order(String name) {
        super(name);
    }

    public Order(String name, String description) {
        super(name, description);
    }

    public BigInteger getDriverId() {
        return driverId;
    }

    public void setDriverId(BigInteger driverId) {
        this.driverId = driverId;
    }

    public BigInteger getClientId() {
        return clientId;
    }

    public void setClientId(BigInteger clientId) {
        this.clientId = clientId;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigInteger getDistance() {
        return distance;
    }

    public void setDistance(BigInteger distance) {
        this.distance = distance;
    }

    public BigInteger getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(BigInteger driverRating) {
        this.driverRating = driverRating;
    }

    public String getDriverMemo() {
        return driverMemo;
    }

    public void setDriverMemo(String driverMemo) {
        this.driverMemo = driverMemo;
    }


    @Override
    public String toString() {
        return "Order{" +
                "driverId=" + driverId +
                ", clientId=" + clientId +
                ", status=" + status +
                ", cost=" + cost +
                ", distance=" + distance +
                ", driverRating=" + driverRating +
                ", driverMemo='" + driverMemo + '\'' +
                '}';
    }
}
