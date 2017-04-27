package com.netcracker.project.study.model.order.route;

/**
 * Created by Mark on 25.04.2017.
 */
public class Route {
    public final int OBJECT_TYPE_ID=7;

    private int orderId;
    private String checkPoint;

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
