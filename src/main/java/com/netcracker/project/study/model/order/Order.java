package com.netcracker.project.study.model.order;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.*;
import com.netcracker.project.study.model.client.Client;
import com.netcracker.project.study.model.driver.Driver;

import java.math.BigDecimal;
import java.math.BigInteger;

@ObjectType(objectTypeId = OrderAttr.OBJECT_TYPE_ID)
public class Order extends Model implements OrderAttr{

    @Reference(attrId = OrderAttr.DRIVER_ID_ATTR)
    private BigInteger driverId;

    @Reference(attrId = OrderAttr.DRIVER_ID_ATTR)
    private Driver driverOnOrder;

    @Reference(attrId = OrderAttr.CLIENT_ID_ATTR)
    private BigInteger clientId;

    @Reference(attrId = OrderAttr.CLIENT_ID_ATTR)
    private Client clientOnOrder;

    @Attribute(attrId = OrderAttr.STATUS_ATTR)
    private @AttrList BigInteger status;

    @Attribute(attrId = OrderAttr.COST_ATTR)
    private @AttrValue BigDecimal cost;

    @Attribute(attrId = OrderAttr.DISTANCE_ATTR)
    private @AttrValue BigDecimal distance;

    @Attribute(attrId = OrderAttr.DRIVER_RATING_ATTR)
    private @AttrValue BigInteger driverRating;

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

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
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

    public Driver getDriverOnOrder() {return driverOnOrder;}

    public void setDriverOnOrder(Driver driverOnOrder) { this.driverOnOrder = driverOnOrder;}

    public Client getClientOnOrder() {return clientOnOrder;}

    public void setClientOnOrder (Client clientOnOrder) { this.clientOnOrder = clientOnOrder;}


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
