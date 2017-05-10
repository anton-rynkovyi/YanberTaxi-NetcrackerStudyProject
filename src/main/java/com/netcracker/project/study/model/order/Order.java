package com.netcracker.project.study.model.order;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;
import com.netcracker.project.study.model.annotations.Reference;
import com.netcracker.project.study.model.client.ClientAttr;
import com.netcracker.project.study.model.driver.DriverAttr;

@ObjectType(objectTypeId = OrderAttr.OBJECT_TYPE_ID)
public class Order extends Model{

    @Reference(attrId = OrderAttr.DRIVER_ID_ATTR)
    private long driverId;

    @Reference(attrId = OrderAttr.CLIENT_ID_ATTR)
    private long clientId;

    @Attribute(attrId = OrderAttr.STATUS_ATTR)
    private int status;

    @Attribute(attrId = OrderAttr.COST_ATTR)
    private int cost;

    @Attribute(attrId = OrderAttr.DISTANCE_ATTR)
    private int distance;

    @Reference(attrId = OrderAttr.DRIVER_RATING_ATTR)
    private long driverRating;

    @Attribute(attrId = OrderAttr.DRIVER_MEMO_ATTR)
    private String driverMemo;

    public Order() {}

    public Order(String name) {
        super(name);
    }

    public Order(String name, String description) {
        super(name, description);
    }

    public long getDriverId() {
        return driverId;
    }

    public void setDriverId(long driverId) {
        this.driverId = driverId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public long getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(long driverRating) {
        this.driverRating = driverRating;
    }

    public String getDriverMemo() {
        return driverMemo;
    }

    public void setDriverMemo(String driverMemo) {
        this.driverMemo = driverMemo;
    }
}
