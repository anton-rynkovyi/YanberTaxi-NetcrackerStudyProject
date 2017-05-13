package com.netcracker.project.study.model.order;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.*;

import java.math.BigInteger;

@ObjectType(objectTypeId = OrderAttr.OBJECT_TYPE_ID)
public class Order extends Model{

    @Reference(attrId = OrderAttr.DRIVER_ID_ATTR)
    private BigInteger driverId;

    @Reference(attrId = OrderAttr.CLIENT_ID_ATTR)
    private BigInteger clientId;

    @Attribute(attrId = OrderAttr.STATUS_ATTR)
    private @AttrList BigInteger status;

    @Attribute(attrId = OrderAttr.COST_ATTR)
    private @AttrValue BigInteger cost;

    @Attribute(attrId = OrderAttr.DISTANCE_ATTR)
    private @AttrValue BigInteger distance;

    @Reference(attrId = OrderAttr.DRIVER_RATING_ATTR)
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

    public BigInteger getCost() {
        return cost;
    }

    public void setCost(BigInteger cost) {
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
}
