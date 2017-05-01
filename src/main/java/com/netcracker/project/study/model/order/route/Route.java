package com.netcracker.project.study.model.order.route;

import com.netcracker.project.study.model.Model;

public class Route extends Model {

    public final int OBJECT_TYPE_ID=7;

    private int orderId;
    private String checkPoint;

    public Route(long objectId){
        this.objectId = objectId;
    }

    public int getOrderId(){
        return orderId;
    }

    public void setOrderId(int orderId){
        this.orderId=orderId;
    }

    public String getCheckPoint(){
        return checkPoint;
    }

    public void setCheckPoint(String checkPoint){
        this.checkPoint=checkPoint;
    }
}
