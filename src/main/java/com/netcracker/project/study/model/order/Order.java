package com.netcracker.project.study.model.order;

import com.netcracker.project.study.model.Model;
import com.netcracker.project.study.model.annotations.Attribute;
import com.netcracker.project.study.model.annotations.ObjectType;
import com.netcracker.project.study.model.annotations.Reference;

@ObjectType(objectTypeId = 3)
public class Order extends Model{

    @Attribute(attrId = OrderAttr.DRIVER_ID_ATTR)
    @Reference(objectTypeId = 1)
    private int driverId;

    @Attribute(attrId = OrderAttr.CLIENT_ID_ATTR)
    @Reference(objectTypeId = 2)
    private int clientId;

    @Attribute(attrId = OrderAttr.STATUS_ATTR)
    private String status;

    @Attribute(attrId = OrderAttr.COST_ATTR)
    private int cost;

    @Attribute(attrId = OrderAttr.DISTANCE_ATTR)
    private int distance;

    @Attribute(attrId = OrderAttr.DRIVER_RATING_ATTR)
    @Reference(objectTypeId = 2, attrId = 5)
    private int driverRating;

    @Attribute(attrId = OrderAttr.DRIVER_MEMO_ATTR)
    private String driverMemo;

    public Order(long objectId){
        this.objectId = objectId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public int getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(int driverRating) {
        this.driverRating = driverRating;
    }

    public String getDriverMemo() {
        return driverMemo;
    }

    public void setDriverMemo(String driverMemo) {
        this.driverMemo = driverMemo;
    }

}
